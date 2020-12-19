package com.mycompany.myapp.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.model.RouteS;
import com.mycompany.myapp.service.MapServiceImpl;

public class APIThreadTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int n =10;
		List<String> list = new ArrayList<String>();
		long startt = System.currentTimeMillis();
		for(int i=0; i<n; i++) {
			final int index =i;
			//공공데이터 서비스		
			String sx ="126.865572139231";
			String sy ="37.5507280806214";
			String ex ="126.996969239236";
			String ey ="37.6107638961532"; 
			pds(sx,sy,ex,ey,index);
			list.add("index= "+index+ " is ended at "+new Date());
		}
		long endd = System.currentTimeMillis();
		System.out.println(n+"번 호출 // 스레드 미적용 // 소요시간  :: "+(endd-startt));
		
//		String start ="126.865572139231,37.5507280806214";등촌
//		String goal = "126.996969239236,37.6107638961532";국민대
		
		MapServiceImpl ms = new MapServiceImpl();
		String[] stl = {"신촌역","이대역","홍대역","등촌역","천호역"};
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<String>> futures = new ArrayList<Future<String>>();
		long start = System.currentTimeMillis();
		
		for(int i=0; i<n; i++) {			
			//카카오 api
//			List<Place> list =ms.keywordSearch(stl[i]);
			final int index =i;
			//공공데이터 서비스
			futures.add(executor.submit(()->{
				String sx ="126.865572139231";
				String sy ="37.5507280806214";
				String ex ="126.996969239236";
				String ey ="37.6107638961532"; 
				pds(sx,sy,ex,ey,index);
//				if(data.length()<300)
//					data = pds(sx,sy,ex,ey);
				return "index= "+index+ "is ended at "+new Date()/*+" "+pds(sx,sy,ex,ey,index).substring(0,4)*/;
			}));
			
		}
		
		executor.shutdown();
		executor.awaitTermination(5000,TimeUnit.MILLISECONDS);
//		for(Future<String> s : futures)
//			s.get();
		long end = System.currentTimeMillis();
		System.out.println(n+"번 호출 // 스레드 적용 // 소요시간  :: "+(end-start));
//		for(Future<String> each : futures) {
//			System.out.println(each.get());
//		}
//		System.out.println(executor.isShutdown());
//		if(!executor.isShutdown())
//			executor.shutdown();
		
	}
	private static final String APPKEY ="WB4qNFdvNtXIjPgY3zrN%2BtsN%2ByX3Wxjp%2BJJP%2F04WrIrs1pe1sj3y3pC2N1FTEzBM9TJM%2FGusxGrIAnep%2F0y9gA%3D%3D";
	
	public static String pds(String sx,String sy,String ex,String ey, int index) {
		HttpURLConnection conn = null;
		StringBuilder sb = null;
		int rrr = index;
		try {
			sb = new StringBuilder();
			//request할 url 만들기
			String basic = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBus?ServiceKey=";
			String opts="&startX="+sx+"&startY="+sy+"&endX="+ex+"&endY="+ey;
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
		
		return rrr+"  "+sb.toString();
	}

}
