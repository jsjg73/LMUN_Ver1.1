package com.mycompany.myapp.json;

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
public class JsonParsing {
	private Place stcoor = null;	
	
	public List<Place> getPlaceInfo(String jsonData) {
		List<Place> stationList = new ArrayList<Place>();
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonOb = (JSONObject) jsonParser.parse(jsonData);
			JSONArray docArray = (JSONArray) jsonOb.get("documents");
			//지하철역 중복 체크 위한 hashset
			Set<String> set = new HashSet<String>();
			for (int i = 0; i < docArray.size(); i++) {
				if(set.size()==5)break;
				JSONObject docOb = (JSONObject) docArray.get(i);
				stcoor = new Place();
				//String pname = docO
				stcoor.setName((String) docOb.get("place_name"));
					//중복 지하철 제거
					String stationName = stcoor.getName().split(" ")[0];
					if(set.contains(stationName))continue;
					set.add(stationName);
					
				stcoor.setX((String) docOb.get("x"));
				stcoor.setY((String) docOb.get("y"));
				stcoor.setAddress((String) docOb.get("address_name"));
				stcoor.setPhone((String) docOb.get("phone"));
				stcoor.setPlace_url((String) docOb.get("place_url"));
				stationList.add(stcoor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stationList;
	}
	
	//20.08.29 view-controller 데이터 전달 개선
	public String josonParsing(List<?> list) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonText = mapper.writeValueAsString(list);
		return jsonText;
	}
	//20.08.29 view-controller 데이터 전달 개선
	public String josonParsing(Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonText = mapper.writeValueAsString(o);
		return jsonText;
	}
	
	
	
	//==============================
	public JSONArray polyPathParsing(String jsonData, Place endplace) {
		JSONArray pathArray = new JSONArray();
		double goalx = Double.parseDouble(endplace.getX());
		double goaly = Double.parseDouble(endplace.getY());
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonOb = (JSONObject) jsonParser.parse(jsonData);
			
			Long result_code = (Long) jsonOb.get("code");
			if(result_code == 0) { // 경로 검색 성공
				JSONObject ob = (JSONObject) jsonOb.get("route");
				JSONArray docArray = (JSONArray)(ob.get("traavoidcaronly"));
				ob = (JSONObject) docArray.get(0);
				//경로 데이터 
				pathArray = (JSONArray) ob.get("path");
			}
			
			//code 37 ::목적지 근처에서 유턴 경로 제거
			double now =100;
			int idx =pathArray.size();
			for(int i=pathArray.size()/2; i<pathArray.size(); i++) {
				JSONArray jarr1 = (JSONArray) pathArray.get(i-1);
				JSONArray jarr2 = (JSONArray) pathArray.get(i);
				double dis = new Distance().pointToLineDistance(
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
			
			//idx 이후 데이터 제거
			while(pathArray.size()>idx) {
				pathArray.remove(idx);
			};
			// code 37 end------------------------------------------------
			
//			1	-	출발지와 도착지가 동일
//			2	-	출발지 또는 도착지가 도로 주변이 아닌 경우
//			3	-	자동차 길찾기 결과 제공 불가
//			4	-	경유지가 도로 주변이 아닌 경우
//			5	-	요청 경로가 매우 긴 경우(경유지를 포함한 직선거리의 합이 1500km이상인 경우)

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathArray;
	}
	
	public JSONObject createGeoJson() {
		JSONObject jsonOb = new JSONObject();
		
		JSONArray arr = new JSONArray();
		jsonOb.put("type", "FeatureCollection");
		jsonOb.put("features", arr);
		
		return jsonOb;
	}
	public void addFeature(JSONArray arr, JSONArray path, String color) {
		if(path.size()==0)return;
		JSONObject element = new JSONObject();
		
		JSONObject geometry = new JSONObject();
		geometry.put("type", "LineString");
		geometry.put("coordinates", path);
		
		JSONObject properties = new JSONObject();
		properties.put("color", color);
		
		element.put("type", "Feature");
		element.put("geometry", geometry);
		element.put("properties",properties);
		
		arr.add(element);
	}
}
