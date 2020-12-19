<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=butimsrvsd"></script>
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<div id="map" style="width:100%;height:400px;"></div>
<script>
var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.3700065, 127.121359),
    zoom: 13
});

var centerPath = ${centerPath};
map.data.addGeoJson(centerPath);

function click(btn){
	 var x = $(btn).data(x);
	 var y = $(btn).data(y);
	 $.ajax({
		    url:"geoJson.do",
		    data :{
		    	"x":x,
		    	"y":y
		    },
		    dataType:'json', 
		    success: test,
			error:function(e){
			  alert("data error"+e);
			}
		}); 
}

function test(geojson){
		map.data.addGeoJson(geojson);
}

/* var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.3700065, 127.121359),
    zoom: 13
});

var centerPath = ${centerPath};
map.data.addGeoJson(geojson);
var pathlist=new Array;
for(var idx = 0; idx < geojson.length; idx++){
	pathlist.push(new naver.maps.LatLng(parseFloat(geojson[idx].y),parseFloat(geojson[idx].x)));
}
  */
</script>
</body>
</html>