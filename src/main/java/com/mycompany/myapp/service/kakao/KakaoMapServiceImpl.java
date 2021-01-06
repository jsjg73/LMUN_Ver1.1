package com.mycompany.myapp.service.kakao;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.json.JsonParsing;
import com.mycompany.myapp.model.Place;

@Service
public class KakaoMapServiceImpl {
	@Autowired
	private KakaoAPI K_api;
	@Autowired
	private JsonParsing par;
	public List<Place> getPlaceList(String code, String option){
		
		// 1. url 만들기
		URL url = categorySearchURL("SW8", option);
		
		// 2. api 요청
		String data = K_api.getStationCoord(url);
		
		// 3. parsing
		List<Place> endPlaceList = par.getPlaceInfo(data);
		
		return endPlaceList;
	}
	
	private final String URL_HOME = "https://dapi.kakao.com";
	private final String URL_CATEGORY = "/v2/local/search/category.json";
	private final String URL_KEYWORD = "/v2/local/search/keyword.json";
	private final String URL_ADRESS = "/v2/local/search/address.json";

	
	public URL getURL(String url_, String options) {
		StringBuilder sb = new StringBuilder();
		sb.append(url_);
		StringTokenizer st = new StringTokenizer(options, "/");
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken()).append("=").append(st.nextToken()).append("&");
		}

		// 주소 확인용 디버깅 코드
		String final_request_url = sb.toString();
		URL url = null;
		try {
			url = new URL(final_request_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	public URL categorySearchURL(String categoryCode) {
		String url = URL_HOME + URL_CATEGORY + "?";
		String options = "category_group_code/" + categoryCode;
		
		return getURL(url, options);
	}

	public URL categorySearchURL(String categoryCode, String option) {
		String url = URL_HOME + URL_CATEGORY + "?";
		String options = "category_group_code/" + categoryCode + "/" + option;
		return getURL(url, options);
	}

	public URL keywordSearchURL(String query) {
		String url = URL_HOME + URL_KEYWORD + "?";
		String options = null;
		try {
			options = "query/" + URLEncoder.encode(query, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getURL(url, options);
	}

	public URL keywordSearchURL(String query, String option) {
		String url = URL_HOME + URL_KEYWORD + "?";
		String options = null;
		try {
			options = "query/" + URLEncoder.encode(query, "utf-8") + "/" + option;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getURL(url, options);
	}

	public URL addressSearchURL(String address) {
		String url = URL_HOME + URL_ADRESS + "?";
		String options = null;
		try {
			options = "query/" + URLEncoder.encode(address, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getURL(url, options);
	}

	public URL addressSearchURL(String address, String option) {
		String url = URL_HOME + URL_ADRESS + "?";
		String options = null;
		try {
			options = "query/" + URLEncoder.encode(address, "utf-8") + "/" + option;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getURL(url, options);
	}
}
