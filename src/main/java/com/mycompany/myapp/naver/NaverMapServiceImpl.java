package com.mycompany.myapp.naver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.service.Distance;
import com.mycompany.myapp.service.MapService;

@Service
public class NaverMapServiceImpl implements MapService {
	@Autowired
	private NaverAPI N_api;
	@Autowired
	private NaverJsonParsing par;
	
	public JSONArray[] getPolyPathArr(List<Place> startPlaceList, Place endPlace) throws InterruptedException, ExecutionException {
		
		//array for path data, to the each destination
		JSONArray[] arr = new JSONArray[startPlaceList.size()];
		
		//connect naver api and request data
		String[] apiReqData = requestNaverAPI(startPlaceList, endPlace);
		
		//stringArr to jsonArr
		for(int i=0; i<arr.length; i++) {
			if(apiReqData[i]!=null) {
				// 
				arr[i]= par.polyPathParsing(apiReqData[i], endPlace);
			}
		}
		
		return arr;
	}
	
	//
	public String[] requestNaverAPI(List<Place> startPlaceList, Place endPlace) throws InterruptedException, ExecutionException {
		String[] list = new String[startPlaceList.size()];
		
		String goal = new StringBuilder()
				.append(endPlace.getX())
				.append(",")
				.append(endPlace.getY())
				.toString();
		
		
		// thread for each destinations
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for(int i=0; i<startPlaceList.size(); i++) {
			Place p = startPlaceList.get(i);
			String start = new StringBuilder().append(p.getX())
					.append(",").append(p.getY()).toString();
			final int idx =i;
			futures.add(executor.submit(()->{
				// request NaverAPI data
				String re = null;
				try {
					re = N_api.getPath(start, goal);
				} catch (InterruptedException e) {
					// naver api request has some error
				}
				return re;
			}));
		}
		
		executor.shutdown();
		executor.awaitTermination(3000,TimeUnit.MILLISECONDS); // 3 sec is allowed
		
		//insert thread result. 
		for(int i=0; i<futures.size(); i++) {
			if(futures.get(i).cancel(true)) { // naver api request takes more 3 sec
				//naver api timeout( 3 sec )
			}else {
				if(futures.get(i)!=null)
					list[i]=futures.get(i).get();
			}
		}
		return list;
	}
	
	
	@Autowired
	private Distance distance;
	//remove garbage data ( near the destinations )
	public void removeGarbagePath(JSONArray[] arr, Place endPlace) {
		
		double goalx = Double.parseDouble(endPlace.getX());
		double goaly = Double.parseDouble(endPlace.getY());
		
		for(JSONArray pathArray : arr) {
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
		}
		
	}
	
	public JSONObject convertGeoJson(JSONArray[] pathArr) {
		JSONObject jsonObject = par.createGeoJson();
		JSONArray arr = (JSONArray) jsonObject.get("features");
		for(JSONArray path : pathArr) {
			if(path!=null)
			par.addFeature(arr, path);
		}
		return jsonObject;
	}
}
