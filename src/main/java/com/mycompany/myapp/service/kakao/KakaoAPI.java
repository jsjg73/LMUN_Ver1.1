package com.mycompany.myapp.service.kakao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.json.JsonParsing;
import com.mycompany.myapp.model.Place;

@Service
public class KakaoAPI {
	@Autowired
	private JsonParsing par;
	
	@Value("${kakao.accessKey}")
	private String accessKey;
	
	// REST API에 요청해서 json 형식 데이터 받아올 부분
		public String getStationCoord(URL url) {
			HttpURLConnection conn = null;
			StringBuilder sb = null;
			try {
				String Authorization = "KakaoAK " + accessKey;

				conn = (HttpURLConnection) url.openConnection();
				// Request 형식 설정
				conn.setRequestMethod("GET");
				// 키 입력
				conn.setRequestProperty("Authorization", Authorization);

				// 보내고 결과값 받기
				// 통신 상태 확인 코드.
				int responseCode = conn.getResponseCode();
				if (responseCode == 400) {
					System.out.println("kakao connection :: 400 error");
				} else if (responseCode == 401) {
					System.out.println("401:: Wrong X-Auth-Token Header");
				} else if (responseCode == 500) {
					System.out.println("500:: kakao server error");
				} else { // 성공 후 응답 JSON 데이터받기
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
//			par = new JsonParsing();
//			return par.getPlaceInfo(sb.toString());
			return sb.toString();
		}
}
