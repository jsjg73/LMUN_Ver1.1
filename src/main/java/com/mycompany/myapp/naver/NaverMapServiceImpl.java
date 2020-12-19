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
			arr[i]= par.polyPathParsing(apiReqData[i], endPlace);
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
		
		int idx=0;
		
		// thread for each destinations
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for( Place p : startPlaceList) {
			String start = new StringBuilder().append(p.getX())
					.append(",").append(p.getY()).toString();
			
			futures.add(executor.submit(()->{
				// request NaverAPI data
				return N_api.getPath(start, goal);
			}));
		}
		
		executor.shutdown();
		boolean allThreadDone = executor.awaitTermination(3000,TimeUnit.MILLISECONDS);
		
		//insert thread result. 
		for(int i=0; i<futures.size(); i++) {
			//예외처리 필요
			if(!allThreadDone && futures.get(i).isDone()) {
				// 어떻게 처리할지?
				// 시간초과로 데이터 못 불러옴
			}else {
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
			par.addFeature(arr, path);
		}
		return jsonObject;
	}
}
