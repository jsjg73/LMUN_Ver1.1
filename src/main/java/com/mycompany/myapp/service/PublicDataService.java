package com.mycompany.myapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.json.XmlParsing;
import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.model.RouteS;
@Service
public class PublicDataService {
	//김재성
	private final String APPKEY ="ysUT4N0M2IspncGRIApzEMkgknQKgXN6UksZY3xYK0VasLokEtQDABvNPEHWOePddgtXtp4rwYI0pIWiR6H37A%3D%3D";
	
	//은지님
	//private final String APPKEY ="WB4qNFdvNtXIjPgY3zrN%2BtsN%2ByX3Wxjp%2BJJP%2F04WrIrs1pe1sj3y3pC2N1FTEzBM9TJM%2FGusxGrIAnep%2F0y9gA%3D%3D";
	
	//가을님
	//private final String APPKEY ="Npzb8zhSrUiFiJVKfHmnCmmTA7xSY80cx5vG4bAsQdpcA3q2txnx63ai5GhDihn4U5w07imTLxgkkOG6NKYrvA%3D%3D";
	public void getPath(Place start,Place end, String transport, RouteS rs) {
		HttpURLConnection conn = null;
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			//request할 url 만들기
			String basic = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBy"+transport+"?ServiceKey=";
			String opts="&startX="+start.getX()+"&startY="+start.getY()+"&endX="+end.getX()+"&endY="+end.getY();
			String final_request_url = basic+APPKEY+opts;
			
			//url 객체 생성
			URL url = new URL(final_request_url);
			
			conn = (HttpURLConnection) url.openConnection();
			// Request 형식 설정
			conn.setRequestMethod("GET");
			// 키 입력

			// 보내고 결과값 받기
			// 통신 상태 확인 코드.
			int responseCode = conn.getResponseCode();
			if (responseCode == 400) {
				System.out.println(
						"400 error :: public data service");
			} else if (responseCode == 401) {
				System.out.println("401 error :: public data service");
			} else if (responseCode == 500) {
				System.out.println("500 error :: public data service");
			} else { // 성공 후 응답 XML 데이터 문자열로 받기
				sb = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 문자열 parsing 처리
		
		new XmlParsing().getRouteInfo(rs,transport, sb.toString());
	}
	
	
}
