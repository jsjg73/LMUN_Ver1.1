package com.mycompany.myapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.MapDAO;
import com.mycompany.myapp.json.JsonParsing;
import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.model.RouteM;
import com.mycompany.myapp.model.RouteS;
import com.mycompany.myapp.naver.NaverAPI;
import com.mycompany.myapp.service.kakao.KakaoMapServiceImpl;

@Service
public class MapServiceImpl implements MapService {

	@Autowired
	private MapDAO md;
	@Autowired
	private JsonParsing par;
	@Autowired
	private PublicDataService pds;
	@Autowired
	private NaverAPI driving;
	@Autowired
	private KakaoMapServiceImpl kms;


	public List<Place> getPlaceList(String categoryCode, String option) {
		return kms.getPlaceList(categoryCode, option);
	}

	
	

	// 중심 좌표 구하기.
	// 출발지 좌표 값의 평균.
	public Place getCenter(List<Place> placeList) {
		Place p = new Place();
		float n = placeList.size();
		float sumX = 0;
		float sumY = 0;
		for (int i = 0; i < n; i++) {
			sumX += Float.parseFloat(placeList.get(i).getX());
			sumY += Float.parseFloat(placeList.get(i).getY());
		}
		p.setX(Float.toString(sumX / n));
		p.setY(Float.toString(sumY / n));
		p.setName("중심");
		return p;
	}

	
	//공공데이터포털에서 경로와 시간 구해오는 메소드
	//리턴 형식은 '/'와 '#' 를 구분자로하는 문자열 타입
	//각 경로는 '/'로 구분
	//하나의 경로에서 시간 거리 출발지 도착지 등 정보는 '#'로 구분

	public void getFinalPath(RouteS rs,Place startPlace, Place endPlace, String transport) {
		StringBuilder sb = new StringBuilder();
		//PublicDataService pds = new PublicDataService();				
		pds.getPath(startPlace, endPlace, transport, rs);
		
	}
	
	public List<RouteS> getPublicDataPath(List<Place> startList, Place end) throws InterruptedException {
		List<RouteS> list = new ArrayList<RouteS>();
		ExecutorService executor = Executors.newFixedThreadPool(3);
		for(Place p : startList) {
			RouteS rs = new RouteS();
			rs.setDeparture(p.getAddress());
			//pds.getPath(p, end, "Bus", rs);
			rs.setBus_time("NONE");
			rs.setBus_route("NONE");
			list.add(rs);
			executor.submit(()->{
				pds.getPath(p, end, "BusNSub", rs);
			});
			
		}
		executor.shutdown();
		executor.awaitTermination(3000,TimeUnit.MILLISECONDS);
		return list;
	}
	
	//마지막 페이지에 필요한 정보(출발지, 경로1, 경로2, 시간1, 시간2)
	public int finalDBSetting(List<Place> startPlaceList, Place endPlace, RouteM rm) throws InterruptedException{		
		
		rm.setNum(startPlaceList.size());
		md.insertRouteM(rm);
		List<RouteS> list = getPublicDataPath(startPlaceList, endPlace);
		for(RouteS rs : list) {
			rs.setId(rm.getId());
			md.insertRouteS(rs);
		}
		return 0;
	}
	public RouteM routeSearch(String id) {
		
		RouteM rm = md.routeSearch(id);
		
		return rm;
	}
	public void createId(List<Place> list, String spl) {		
		Collections.sort(list, new Comparator<Place>() {
			public int compare(Place o1, Place o2) {
				return o1.getAddress().compareTo(o2.getAddress());
			};
		});
		for(Place p : list) {
			StringBuilder sb = new StringBuilder();
			sb.append(spl);
			sb.append(p.getPlace_url());
			int id = sb.toString().hashCode();
			p.setId(id+"");
			
		}
	}
	public List<RouteS> getRouteList(RouteM r) {
		return md.getRouteList(r);
	}
	
	
}

