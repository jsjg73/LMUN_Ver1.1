package com.mycompany.myapp.naver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.model.NPath;
import com.mycompany.myapp.service.Distance;
import com.mycompany.myapp.service.MapService;

@Service
public class NaverMapServiceImpl implements MapService {
	@Autowired
	private NaverAPI N_api;
	@Autowired
	private NaverJsonParsing par;
	
	public void requestNaverAPI(List<NPath> pathList) throws InterruptedException, ExecutionException {
		
		// thread for each destinations
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future> futures = new ArrayList<Future>();
		for(int i=0; i<pathList.size(); i++) {
			final int idx = i;
			futures.add(executor.submit(()->{
				// request NaverAPI data
				N_api.getPath(pathList.get(idx));
			}));
		}
		
		executor.shutdown();
		executor.awaitTermination(3000,TimeUnit.MILLISECONDS); // 3 sec is allowed
		
		//insert thread result. 
		for(int i=0; i<futures.size(); i++) {
			if(futures.get(i).cancel(true)) { // naver api request takes more 3 sec
				//naver api timeout( 3 sec )
				pathList.get(i).setStCode(999);
				pathList.get(i).setStMsg("timeout");
			}
		}
	}
	
	public void strToJSONArray(List<NPath> pathList) {
		for(NPath o : pathList) {
			par.polyPathParsing(o);
		}
	}
	
	@Autowired
	private Distance distance;
	//remove garbage data ( near the destinations )
	public void removeGarbagePath(List<NPath> pathList) {
		
		double goalx = Double.parseDouble(pathList.get(0).getEx());
		double goaly = Double.parseDouble(pathList.get(0).getEy());
		
		for(NPath o : pathList) {
			JSONArray pathArray = o.getPath();
			if(pathArray==null)continue;
			double now =100;
			int idx =pathArray.size();
			for(int i=pathArray.size()/2; i<pathArray.size(); i++) {
				JSONArray jarr1 = (JSONArray) pathArray.get(i-1);
				JSONArray jarr2 = (JSONArray) pathArray.get(i);
				double dis = distance.pointToLineDistance(
						(double)jarr1.get(0), (double)jarr1.get(1), 
						(double)jarr2.get(0), (double)jarr2.get(1), 
						goalx, goaly);
				if(dis<80.0) {
					if(now>dis) {
						now = dis;
						idx = i;
					}else {
						break;
					}
				}
			}
			
			// remove after idx
//			pathArray = (JSONArray) pathArray.subList(0, idx);
			
			while(pathArray.size()>idx) {
				pathArray.remove(idx);
			}
			o.setPath(pathArray);
		}
		
	}
	
}
