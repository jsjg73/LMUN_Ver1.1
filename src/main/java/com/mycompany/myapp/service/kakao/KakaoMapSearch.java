package com.mycompany.myapp.service.kakao;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class KakaoMapSearch {
	static Logger log = LoggerFactory.getLogger(KakaoMapSearch.class);
	protected Map<String, String> options = new HashMap<String, String>();
	protected final String URL_HOME = "https://dapi.kakao.com/";
	protected String SEARCH_TYPE;
	
	public URL getURL() {
		StringBuilder sb = new StringBuilder();
		sb.append(URL_HOME)
			.append(SEARCH_TYPE)
			.append(writeOptions());
		
		
		String final_request_url = sb.toString();
		log.info(final_request_url);
		URL url = null;
		try {
			url = new URL(final_request_url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	private String writeOptions() {
		String str ="?";
		
		for(Map.Entry<String, String> entry : options.entrySet()) {
			str+=entry.getKey()+"="+entry.getValue()+"&";
		}
		
		return str;
	}
}
