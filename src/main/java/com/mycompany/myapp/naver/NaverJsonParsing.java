package com.mycompany.myapp.naver;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.model.DrawingPath;
import com.mycompany.myapp.model.NPath;
import com.mycompany.myapp.model.Place;
@Service
public class NaverJsonParsing {
	
	
	public void polyPathParsing(NPath path) {
		JSONArray pathArray = null;
		if(path.getStCode()!=200)return;
		double goalx = Double.parseDouble(path.getEx());
		double goaly = Double.parseDouble(path.getEy());
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonOb = (JSONObject) jsonParser.parse(path.getRawPath());
			
			long result_code = (Long) jsonOb.get("code");
			
			if(result_code == 0) { // 경로 검색 성공
				pathArray = new JSONArray();
				JSONObject ob = (JSONObject) jsonOb.get("route");
				JSONArray docArray = (JSONArray)(ob.get("traavoidcaronly"));
				ob = (JSONObject) docArray.get(0);
				//경로 데이터 
				pathArray = (JSONArray) ob.get("path");
				path.setPath(pathArray);
			}else { 
				// 경로 검색 실패
				path.setStCode(result_code);
				path.setStMsg((String)jsonOb.get("message"));
				//			1	-	출발지와 도착지가 동일
				//			2	-	출발지 또는 도착지가 도로 주변이 아닌 경우
				//			3	-	자동차 길찾기 결과 제공 불가
				//			4	-	경유지가 도로 주변이 아닌 경우
				//			5	-	요청 경로가 매우 긴 경우(경유지를 포함한 직선거리의 합이 1500km이상인 경우)
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
