package com.mycompany.myapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.myapp.json.JsonParsing;
import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.model.RouteM;
import com.mycompany.myapp.model.RouteS;
import com.mycompany.myapp.naver.NaverMapServiceImpl;
import com.mycompany.myapp.service.MapServiceImpl;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MapAction {
	
	@Autowired
	private MapServiceImpl mapServices;
	@Autowired
	private NaverMapServiceImpl nms;
	@Autowired
	private JsonParsing jsonparser;
	@RequestMapping("test.do")
	public String home_push(HttpSession session, HttpServletRequest request) {
		return "map/home";
	}
	
	//ajax�샇異�
	@RequestMapping("geoJson.do")
	@ResponseBody
	public JSONObject geoJson(HttpSession session, Place endPlace, Model model) throws InterruptedException, ExecutionException {
		List<Place> startPlaceList = (List<Place>)session.getAttribute("startPlaceList");
		
		// 해결과제 : api 호출이 실패(시간초과  등)했을 때 어떻게 처리할지...
		// get path data from naver api
		JSONArray[] pathArr= nms.getPolyPathArr(startPlaceList, endPlace);
		// remove garbage data ( near the destinations )
		nms.removeGarbagePath(pathArr, endPlace);
		
		// convert to geoJson form ( front javascript wants geoJson form data )
		return nms.convertGeoJson(pathArr);
	}
	
	@RequestMapping("publicDataService.do")
	@ResponseBody
	public String publicDataService(HttpSession session, Place endplace, Model model) throws InterruptedException, ExecutionException, JsonProcessingException {
		List<Place> startPlaceList = (List<Place>)session.getAttribute("startPlaceList");
		// 媛� �썑蹂댁��뿉 ���빐�꽌 �냼�슂�떆媛� 蹂댁뿬二쇨린.		
		// 怨듦났�뜲�씠�꽣�룷�꽭�뿉�꽌 寃쎈줈 李얠븘�삤湲�
		// �븘�슂�븳 �젙蹂� : 異쒕컻吏� �젙蹂�, �룄李� �썑蹂댁� �젙蹂�
		List<RouteS> jsonPath = mapServices.getPublicDataPath(startPlaceList, endplace);
		return jsonparser.josonParsing(jsonPath);
	}
	
	@RequestMapping("session_del.do")
	public String testtest(HttpServletRequest request, Place place, Model model) {
		if(place.getName()==null)return "map/home";
		
		ArrayList<Place> startPlaceList = new ArrayList<Place>();
		int n = place.getName().split(",").length;
		for(int i=0; i<n; i++) {
			Place p = new Place();
			p.setAddress(place.getAddress().split(",")[i]);
			p.setName(place.getName().split(",")[i]);
			p.setX(place.getX().split(",")[i]);
			p.setY(place.getY().split(",")[i]);
			startPlaceList.add(p);
		}
		request.getSession().setAttribute("startPlaceList", startPlaceList);
		
		return "map/home";
	}
	
	@RequestMapping("sendAddr2.do")
	public String sendAddr2(HttpServletRequest request, @ModelAttribute Place place, Model model) throws Exception {
		
		
		ArrayList<Place> startPlaceList = (ArrayList<Place>) place.getPlaces();
		// 異뷀썑�뿉 荑좏궎濡� 蹂�寃쏀빐蹂쇨쾬.
		HttpSession session = request.getSession();
		session.setAttribute("startPlaceList", startPlaceList);		
		
		//---------------------------------以묒젏 醫뚰몴 get--------------------------------
		Place center = mapServices.getCenter(startPlaceList);		
		
		// geoJson �깮�꽦 // 寃쎈줈 洹몃━湲�
		JSONArray[] pathArr= nms.getPolyPathArr(startPlaceList, center); //異쒕컻吏��뱾�뿉�꽌 以묒떖 醫뚰몴源뚯� polyline�쓣 洹몃━湲� �쐞�븳 寃쎈줈諛곗뿴 �뜲�씠�꽣 �슂泥� 諛� �꽭�똿
		JSONObject jsonObject = jsonparser.createGeoJson(); //javascript�뿉�꽌 �궗�슜�븷 geojson�삎�깭濡� 蹂��솚
		JSONArray arr = (JSONArray) jsonObject.get("features");
		for(JSONArray list : pathArr) {
			jsonparser.addFeature(arr, list, "red");
		}
		model.addAttribute("centerPath", jsonObject.toString());
		
		
		//--------------------------------媛�源뚯슫 吏��븯泥좎뿭 5媛� get-------------------------------
		// category_group_code:SW8(吏��븯泥�), page:1, size:15(湲곕낯媛�), radius:2000 �쑝濡� �젣�븳�븯�뿬 �슂泥�
		String option = "x/" + center.getX() + "/y/" + center.getY() + "/page/1/radius/2000";
		List<Place> endplaceList = mapServices.categorySearch("SW8", option);
		
		endplaceList.add(0,	center);//以묒떖 醫뚰몴 異붽�.	
		
		// js�뿉�꽌 �궗�슜�븯湲� �렪�븯寃�  json�삎�떇�쑝濡� 蹂��솚.
		model.addAttribute("jsonEpl", jsonparser.josonParsing(endplaceList));// 異붿쿇吏��뿭 json 蹂��솚 08.29
		model.addAttribute("jsonSpl", jsonparser.josonParsing(startPlaceList));// 異쒕컻吏��뿭 json 蹂��솚08.29
		
		return "map/foundplace2";
	}
	@RequestMapping("category.do")
	public String categorySelect(Place place, Model model, HttpSession session) {

		if(session.getAttribute("startPlaceList")==null) {
			return "member/sessionResult";
		}
		model.addAttribute("place", place);

		//-------------------- 移댄뀒怨좊━蹂� 異붿쿇 �옣�냼 5媛� ----- 08/05 源�媛��쓣  --------------------
		String option = "x/" + place.getX() + "/y/" + place.getY() + "/page/1/size/5/radius/2000";
		
		StringBuilder sb = new StringBuilder();
		List<Place> startPlaceList = (List<Place>)session.getAttribute("startPlaceList");
		for(Place p : startPlaceList)
			sb.append(p.getAddress());
		// CT1 臾명솕�떆�꽕
		List<Place> ct1placeList = mapServices.categorySearch("CT1", option);
		mapServices.createId(ct1placeList, sb.toString());//媛� �룄李⑹��뿉 ���빐 id遺��뿬
		model.addAttribute("ct1placeList", ct1placeList);
		// FD6 �쓬�떇�젏
		List<Place> fd6placeList = mapServices.categorySearch("FD6", option);
		mapServices.createId(fd6placeList, sb.toString());//媛� �룄李⑹��뿉 ���빐 id遺��뿬
		model.addAttribute("fd6placeList", fd6placeList);
		// CE7 移댄럹
		List<Place> ce7placeList = mapServices.categorySearch("CE7", option);
		mapServices.createId(ce7placeList, sb.toString());//媛� �룄李⑹��뿉 ���빐 id遺��뿬
		model.addAttribute("ce7placeList", ce7placeList);
		// AT4 愿�愿묐챸�냼
		List<Place> at4placeList = mapServices.categorySearch("AT4", option);
		mapServices.createId(at4placeList, sb.toString());//媛� �룄李⑹��뿉 ���빐 id遺��뿬
		model.addAttribute("at4placeList", at4placeList);
		//------------------------------------------------------------
		
		return "map/category";
	}
	
	@RequestMapping("route.do")
	public String route(String status, HttpSession session, Place place, RouteM rm, Model model) throws JsonProcessingException, InterruptedException {
		//吏��븯泥�,踰꾩뒪,
		//寃쎈줈 db�뿉 ���옣�맂 �젙蹂� �엳�뒗吏� 泥댄겕.
		RouteM r = mapServices.routeSearch(rm.getId());
		
		//鍮꾩꽦�옣 �젒洹� 泥댄겕
		if(status==null && r == null) {
			return "map/routeResult";	
		}

		if(r == null ) {
			//session.setAttribute("id", id);
			Place endPlace = place;
			List<Place> startPlaceList =(List<Place>) session.getAttribute("startPlaceList");		
			mapServices.finalDBSetting(startPlaceList,endPlace,rm);
			r = mapServices.routeSearch(rm.getId());
			
		}
		List<RouteS> routeList = mapServices.getRouteList(r);
		model.addAttribute("routelist", routeList);
		model.addAttribute("endPlace",r);
		model.addAttribute("end",jsonparser.josonParsing(r));

		return "map/route";
	}
	
}
