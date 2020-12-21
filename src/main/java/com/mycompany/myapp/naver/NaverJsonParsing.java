package com.mycompany.myapp.naver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.service.Distance;
@Service
public class NaverJsonParsing {
	
	public List<Place> getPath(String jsonData) {
		List<Place> list = new ArrayList<Place>();
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonOb = (JSONObject) jsonParser.parse(jsonData);
			String result_code = (String) jsonOb.get("code");
			if(!result_code.equals("0")) {
				return null;
			}
			JSONObject route = (JSONObject) jsonOb.get("route");
			JSONArray docArray = (JSONArray)(jsonOb.get("traoptimal"));
			jsonOb = (JSONObject) docArray.get(0);
			JSONArray pathArray = (JSONArray) jsonOb.get("path");
			Place p = null;
			for (int i = 0; i < pathArray.size(); i++) {
				JSONArray arr = (JSONArray) pathArray.get(i);
				p = new Place();
				p.setX(Double.toString((Double) arr.get(0)));
				p.setY(Double.toString((Double) arr.get(1)));
				list.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public JSONObject createGeoJson() {
		JSONObject jsonOb = new JSONObject();
		JSONArray arr = new JSONArray();
		jsonOb.put("type", "FeatureCollection");
		jsonOb.put("features", arr);
		return jsonOb;
	}
	public void addFeature(JSONArray features, JSONArray path) {
		if(path.size()==0)return;
		JSONObject element = new JSONObject();
		
		JSONObject geometry = new JSONObject();
		geometry.put("type", "LineString");
		geometry.put("coordinates", path);
		
		element.put("type", "Feature");
		element.put("geometry", geometry);
		
		features.add(element);
	}
	public JSONArray polyPathParsing(String jsonData, Place endplace) {
		JSONArray pathArray = null;
		double goalx = Double.parseDouble(endplace.getX());
		double goaly = Double.parseDouble(endplace.getY());
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonOb = (JSONObject) jsonParser.parse(jsonData);
			
			Long result_code = (Long) jsonOb.get("code");
			
			if(result_code == 0) { // 경로 검색 성공
				pathArray = new JSONArray();
				JSONObject ob = (JSONObject) jsonOb.get("route");
				JSONArray docArray = (JSONArray)(ob.get("traavoidcaronly"));
				ob = (JSONObject) docArray.get(0);
				//경로 데이터 
				pathArray = (JSONArray) ob.get("path");
			
			}else { // 경로 검색 실패
				
//			1	-	출발지와 도착지가 동일
//			2	-	출발지 또는 도착지가 도로 주변이 아닌 경우
//			3	-	자동차 길찾기 결과 제공 불가
//			4	-	경유지가 도로 주변이 아닌 경우
//			5	-	요청 경로가 매우 긴 경우(경유지를 포함한 직선거리의 합이 1500km이상인 경우)
			}
			
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathArray;
	}
	
}
