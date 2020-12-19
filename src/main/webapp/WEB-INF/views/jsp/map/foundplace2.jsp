<%@page import="org.springframework.ui.Model"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta charset="UTF-8">
<title>추천 후보지를 보여드립니다.</title>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
body {
	font-family: 'Nanum Gothic', sans-serif;
}
h1, h2, h3, h4, h5, h6 {
	font-family: 'Nanum Gothic', sans-serif;
	letter-spacing: 5px;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="<%=request.getContextPath()%>/js/friend.js"></script>
<script src="<%=request.getContextPath()%>/js/all.min.js"></script>
<script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=butimsrvsd"></script>

</head>
<body>
	<!-- Navbar (sit on top) -->
	<div class="w3-top w3-light-blue">
		<div class="w3-bar w3-white w3-padding w3-card"
			style="letter-spacing: 4px;">
			<a href="index.jsp" class="w3-bar-item w3-button">우리 지금 만나</a>
			<!-- Right-sided navbar links. Hide them on small screens -->
			<div class="w3-right w3-hide-small">
				<!-- 로그인 세션 있으면 회원정보로-->
				<c:if test="${empty email}">
					<a href="member_login.do" class="w3-bar-item w3-button">로그인</a>
					<a href="member_join.do" class="w3-bar-item w3-button">회원가입</a>
				</c:if>
				<c:if test="${not empty email}">
					<div class="w3-dropdown-hover">
						<c:if test="${fn:length(fr_push) > 0}">
							<button id="fr_request" class="w3-button">친구요청&ensp;<span class="badge badge-light">${fn:length(fr_push)}</span></button>
							<div class="w3-dropdown-content w3-bar-block w3-card-4">
								<c:forEach var="fr_push" items="${fr_push}">
									<div class="w3-bar-item fr_push_list">
										<div class="w3-row">
											<div class="w3-col s6">
												<span class="w3-bar-item ontop">${fr_push.inviter}</span>
											</div>
											<div class="w3-col s3">
												<button class="w3-bar-item w3-button ontop" onclick="frpush('${fr_push.inviter}','1')">O</button>
											</div>
											<div class="w3-col s3">
												<button class="w3-bar-item w3-button ontop" onclick="frpush('${fr_push.inviter}','2')">X</button>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</c:if>
					</div>
					<a href="member_info.do" class="w3-bar-item w3-button">회원정보</a>
					<a href="member_logout.do" class="w3-bar-item w3-button">로그아웃</a>
				</c:if>
			</div>
			<div class="w3-right w3-hide-large w3-hide-medium">
				<!-- 로그인 세션 있으면 회원정보로-->
				<c:if test="${empty email}">
					<a href="member_login.do" class="w3-bar-item w3-button"> <i
						class="fa fa-user-circle fa-lg" aria-hidden="true"> </i></a>
				</c:if>
				<c:if test="${not empty email}">
					<a href="member_info.do" class="w3-bar-item w3-button"> <i
						class="fa fa-user-circle fa-lg" aria-hidden="true"> </i></a>
				</c:if>
			</div>
		</div>
	</div>
	<div class="container-sm " align="center" style="margin-top: 80px">
		<!-- <div id="map_div" class="container"
			style="margin-top: 80px; width: 100%; height: 350px;"></div> -->
		<div id="map" style="width:100%;height:400px;"></div>
		<div class='container-sm'>


			<div class="container" style="margin-top: 20px; margin-bottom: 20px">
				<span id="selected_location"></span>
				<ul class="nav nav-tabs">
					<li class="nav-item">
						<!--          <a class="nav-link active" --> <!--             data-toggle="tab" href="#home" onclick="drawmap(1)">location1</a> -->
					</li>

				</ul>
			</div>
			<form method="get" action="category.do">
				<input type="hidden" id="selected_x" name="x" value=""> <input
					type="hidden" id="selected_y" name="y" value=""> <input
					type="hidden" id="selected_name" name="name" value="">
				<div class="tab-content">
					<div class="tab-pane container active" id="home"></div>

				</div>

				<input class="btn btn-dark" type='submit' value='결정'>
			</form>
		</div>
	</div>
	<br>
	<br>
	<footer class="w3-center w3-light-grey w3-padding-32">
		Contact Us 
		<div class="btn-group">
			<button onclick="location.href='https://github.com/mynameiseunji/mapmap'" title="github" class='btn'><i class="fab fa-github-square fa-lg"></i></button>		
		</div>
	</footer>
	
	<script>
	
	var epl = ${jsonEpl};//endplacelist
   	var spl = ${jsonSpl};//startplacelist
   	/* var pathToEpl = ${path}; */
   	
	var map = new naver.maps.Map('map', {
	    center: new naver.maps.LatLng(epl[0].y, epl[0].x),
	    zoom: 15
	});
   	
   	//출발지 마커 배열
	var start_markers = [];
   	//출발지 마커 올리기
	for(var i=0; i<spl.length; i++){
		var marker = new naver.maps.Marker({ 
				position : new naver.maps.LatLng(spl[i].y, spl[i].x),
				map : map
		});
		start_markers.push(marker);
	}
    var destination_marker= new naver.maps.Marker({ 
			position : new naver.maps.LatLng(epl[0].y, epl[0].x),
			map : map,
			icon :{
				url :'<%=request.getContextPath()%>/js/pngwing.com.png',
				scaledSize: new naver.maps.Size(23, 35)
			}
	});
	var centerPath = ${centerPath};
	var nowGeojson =centerPath; // 기존 레이어 저장  -> 이후에 삭제할때 필요함.
	map.data.addGeoJson(centerPath);

	var arrGeojson = []; // 경로 그려줄 geoJson 데이터 배열 // ajax 통해  서버로부터 받아옴.
	
	
	<!-- 경로 polyline 그려줄  경로 배열 ajax 호출-->
	<!-- naver driving api-->
	function tab_click(k){
		//현재 함수 실행 될때 발생되는 이벤트 리스트 적어놓기.
		var headd = '';
		var bodyy ='';
		
		//이미 호출한적 있는지 확인.
  	  var hasData = $('#menu'+k+'_head').html()=="";
  	  alert(hasData);
  	  if(hasData){
		 $.ajax({
			    url:"geoJson.do",
			    data :{
			    	"x":epl[k].x,
			    	"y":epl[k].y
			    },
			    dataType:'json', 
			    success: function(data){
			    	//경로 그릴 좌표 배열 데이터 : data
			    	test(data, k);
			    	
			    	arrGeojson[k]=data;
			    	
			    	headd ='<h1>'+epl[k].name+'</h1>';
			    	document.getElementById('menu'+k+'_head').innerHTML = headd;
			    	//console.log(headd);
			        var posi = new naver.maps.LatLng(epl[k].y, epl[k].x);
			        map.setCenter(posi);
			        destination_marker.setPosition(posi);
			    },
				error:function(e){
				  alert("data error"+e);
				}
			}); 
		 $.ajax({
			    url:"publicDataService.do",
			    data :{
			    	"x":epl[k].x,
			    	"y":epl[k].y
			    },
			    dataType:'json', 
			    success: function(data){
			    	//경로 그릴 좌표 배열 데이터 : data
			    	for(var i=0; i<data.length; i++){
			    		bodyy += '<p>'+(i+1)+'번 소요시간:'+data[i].complex_time+'분</p>';	
			    		
			    	}
			    	document.getElementById("menu"+k+"_body").innerHTML = bodyy;
			    },
				error:function(e){
				  alert("data error"+e);
				}
			});
		  //.innerHTML = headd+bodyy;
		  //console.log(headd+bodyy);
  	  }else{
		test(arrGeojson[k],k);
		
		//중심 옮기기
	  	var posi = new naver.maps.LatLng(epl[k].y, epl[k].x);
        map.setCenter(posi);
        destination_marker.setPosition(posi);
  	  }
	}
	
	
	function test(geojson, k){
		if(nowGeojson != null){
			map.data.removeGeoJson(nowGeojson);//기존 레이어 삭제
		}
		nowGeojson = geojson;
		map.data.addGeoJson(geojson);// 새 레이어 올리기
	}
	
      /* var times = new Array();
      //각 도착지에 대한 친구들의 정보 종합.
      for(var i=0; i<pathToEpl.length; i++){
         var str = "";
         var path = pathToEpl[i];
         
         for(var j=0; j<path.length; j++){
            if(path[j].complex_time=="NONE"){
               str += '<p>출발지 '+(j+1)+'번 : 이용할 수 있는 대중 교통이 없습니다.';
            }else{            	
               str += '<p>출발지'+(j+1)+'번 예상 이동시간(대중교통)  : '+path[j].complex_time+'분</p>';   
            }            
         }
         times.push(str);
      } */
      
      
      //append.
      $('#selected_x').attr('value',epl[0].x);
      $('#selected_y').attr('value',epl[0].y);
      $('#selected_name').attr('value',epl[0].name);
      
      // .tab-content 자손태그로 추가(반복)
      for(var i=1; i<epl.length; i++){
         
         var posi_li_fade = '<div class="tab-pane container fade" id="menu'+i+'"><h1 id="menu'+i+'_head"></h1><div id="menu'+i+'_body"></div></div>';/* +
                     '<h1>'+epl[i].name+'</h1>'+
                     times[i-1]+'</p><span data-x="'
                     +epl[i].x+'" data-y="'+epl[i].y+'"></span>';
 */                     
         var posi_li_nav ='<li class="nav-item">'+      
					         '<span data-name="'+epl[i].name+
					         '" data-x="'+epl[i].x+
					         '" data-y="'+epl[i].y+
					         '"></span>' +
					         '<a id= "loc' + i + '" class="nav-link" '+
					         'data-toggle="tab" href="#menu' + i + '" onclick="tab_click(' + i + ')" >' +
					         epl[i].name+'</a></li>';
         
         
         $('.tab-content').append(posi_li_fade);
         $('.nav.nav-tabs').append(posi_li_nav);
      }
        
	</script>

</body>

</html>
