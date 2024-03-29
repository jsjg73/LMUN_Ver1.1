
/*
 curl "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=127.1058342,37.359708&goal=129.075986,35.179470&option=trafast" \
	-H "X-NCP-APIGW-API-KEY-ID: {애플리케이션 등록 시 발급받은 client id 값}" \
	-H "X-NCP-APIGW-API-KEY: {애플리케이션 등록 시 발급받은 client secret값}" -v
 	
 * */
package com.mycompany.myapp.naver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.model.DrawingPath;
import com.mycompany.myapp.model.NPath;
@Service
public class NaverAPI{
	
	@Value("${naver.url}")
	private String URL ;
	@Value("${naver.id}")
	private String ID ;
	@Value("${naver.accessKey}")
	private String KEY;

	public void getPath(NPath path) {
		HttpURLConnection conn = null;
		StringBuilder sb = null;
		
		String start = concatcomma(path.getSx(), path.getSy());
		String goal = concatcomma(path.getEx(), path.getEy());
		
		try {
			String final_request_url = new StringBuffer()
					.append(URL).append("?")
					.append("start=").append(start).append("&")
					.append("goal=").append(goal)
					.append("&option=traavoidcaronly")
					.toString();
			URL url = new URL(final_request_url);
			
			conn = (HttpURLConnection) url.openConnection();
			// Request 형식 설정
			conn.setRequestMethod("GET");
			// 키 입력
			conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ID);
			conn.setRequestProperty("X-NCP-APIGW-API-KEY", KEY);
			
			// 보내고 결과값 받기
			// 통신 상태 확인 코드.
			int responseCode = conn.getResponseCode();
			if (responseCode == 400) {
				path.setStCode(400);
				path.setStMsg("naver api connection :: 400 error");
			} else if (responseCode == 401) {
				path.setStCode(401);
				path.setStMsg("401:: Wrong X-Auth-Token Header");
			} else if (responseCode == 500) {
				path.setStCode(500);
				path.setStMsg("500::server error");
			} else { // 성공 후 응답 JSON 데이터받기
				path.setStCode(200);
				path.setStMsg("OK");
				sb = new StringBuilder();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				path.setRawPath(sb.toString());
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String concatcomma(String x, String y) {
		String str = new StringBuilder()
				.append(x)
				.append(",")
				.append(y)
				.toString();
		return str;
	}
}
