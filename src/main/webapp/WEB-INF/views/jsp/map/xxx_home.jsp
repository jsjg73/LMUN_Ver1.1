<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Bootstrap Example</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0b6c81e5d496e486ca93f4d82d0a0027&libraries=services"></script>
<script src="https://code.jquery.com/jquery-3.5.0.js"></script>

<script>
	/*
	로그인 세션 처리 
	로그인 세션 있으면(컨트롤러에서 친구목록, 로그인 세션 넘어옴)
		1.버튼 value '로그인' => '회원 정보'로 변경
		2.친구 목록 리스트 보여주기. (main.jsp의 var fl 변수 참조)
	*/
	//친구목록 세션에 있을때
	var fl = '${friend_List}'.split("#");
	console.log(fl);
	fl.pop();
	$(document).ready(function(){
		console.log('${sessionScope.email}'+"있습니다.");
		if('${sessionScope.email}'!=""){
			$("#login").append("친구 목록 있음/ 로그인 됨<br>");			
			var tag =""; //20라인에서 append될 태그 변수
			for(var i=0; i<fl.length; i++){
				//태그 쌓기
				tag +=fl[i]+'<br>';
			}
			$("#login").append(tag);
		}else{
			$("#login").append("친구 목록 없음/ 로그인 안됨");			
			
		}
	});
	
	
	
	var keyword = "";
	var places = new kakao.maps.services.Places();
	var search_type = "keyword";

	// 지도 중심위치 설정
	var latlng = new kakao.maps.LatLng(37.5668260054857, 126.978656785931);
	var options = {
		location : latlng,
		size : 5
	};
	$(document).ready(function() {
		$('.btn-success').click(function() {
			check = /[a-zA-Z0-9가-힣]/;  //검색 형식 검사(숫자, 영어 대소문자, 한글(자음따로 모음따로는 검색 x))
			keyword = $("#bt").val();
			if (keyword.trim() == "") {
				alert("검색어를 입력해주세요.");
				return false;
			}
			if (!check.test(keyword.trim())) { //검색어 유효성 검사
				alert("검색어가 유효하지 않습니다.");
				return false;
			}
			//option으로 검색량 조절하기.
			places.keywordSearch(keyword, callback, options);
		});
		$('#check_data').click(function() {
			if ($('#table_part tr').length < 2) {
				alert("주소지 정보를 2개 이상 입력해주세요");
				return false;
			}
			// controller에서 객체 리스트로 매핑하기위한 코드.
			// submit 버튼 눌렀을 때 작동하는 이벤트
			// name을 places[0].x,  places[0].y .. 처럼 설정해주면
			// controller에서 dto 리스트로 받을 수 있음
			// 참고  http://noveloper.github.io/blog/spring/2015/02/16/how-mapping-to-model-arrayvalue.html
			$("#form .place_values").each( function (index) {
	        	$(this).find("input[name=x]").attr("name", "places[" + index + "].x");
	        	$(this).find("input[name=y]").attr("name", "places[" + index + "].y");
	        	$(this).find("input[name=name]").attr("name", "places[" + index + "].name");
	        	$(this).find("input[name=address]").attr("name", "places[" + index + "].address");
	    	})
	    				
		});
	});
	// 전송 버튼 누를 경우 : 주소지 정보 2개 이상 입력 필수
	
	var callback = function(result, status) {
		if (status === kakao.maps.services.Status.OK) {
			//console.log(search_type);
			$(".modal-body table").empty();
			for ( var i in result) {

				var p_name = "";
				if (search_type == 'keyword') {
					p_name = result[i].place_name;
				} else { //search_type='geo'
					p_name = result[i].address_name;
				}

				var str = "<tr><td>"
						+ p_name
						+ "<br>"
						+ result[i].address_name
						+ "<div class='place_values'><input type='hidden' name='name' value='" + p_name +
						"'/><input type='hidden' name='x' value='" + result[i].x +
						"'/><input type='hidden' name='address' value='"+ result[i].address_name +
						"'/><input type='hidden' name='y' value='" + result[i].y 
						+"'></div></td><td align='right'><input type='button' class='btn btn-info' onclick='down(this)' value='선택'></td></tr>";
				$(".modal-body table").append(str);
			}
		} else if (search_type == 'keyword') {//keyword 검색 결과가 없을 때 진입
			search_type = "geo";
			var geocoder = new kakao.maps.services.Geocoder();
			return geocoder.addressSearch(keyword, callback);
		} else { // keyword 와 address 모두 검색 결과가 없을 때 진입
			$(".modal-body").empty();
			$(".modal-body").append("검색 결과가 없습니다.");
		}

		search_type = "keyword";
	};

	//출발지 선택 팝업에서 선택 버튼을 누를 때 실행
	function down(btn) {
		
		//클릭한 input 태그의 부모 태그 중에 가장 가까운 <tr>태그를 선택
		item = $(btn).closest('tr');

		if ($('div.list input[name="x"]').length == 10) {
			alert("최대10개");
		} else {
			$(btn).attr('onclick', 'remove_addr(this)');
			$(btn).attr('value', '삭제');
			$(btn).attr('class', 'btn btn-outline-danger btn-sm');

			var d = "<tr>" + item.html() + "</tr>";
			$('form div.list table').append(d);
			$('button.close').click();
		}
	}
	
	// 선택된 출발지에서 삭제 버튼 누를 때 실행
	function remove_addr(it) {
		$(it).closest('tr').remove();
	}
	// 	places.keywordSearch('판교 치킨', callback);

	//뒤로가기 이벤트 감지 코드
	// 참고 링크 :: https://dev-t-blog.tistory.com/9
	window.onpageshow = function(event) {
		if (event.persisted
				|| (window.performance && window.performance.navigation.type == 2)) {
			// Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
			console.log("뒤로가기로 들어왔습니다.");
			location.reload();
		} else {
			console.log("새로 진입123");
			location.reload();
		}
	}
</script>
</head>
<body>
	<nav class="navbar navbar-expand-sm bg-dark navbar-dark fixed-top">
		<a class="navbar-brand" href="#">Logo</a>
		<ul class="navbar-nav">
			<li class="nav-item"><a class="nav-link" href="member_login.do">로그인</a></li>
			<li class="nav-item"><a class="nav-link" href="#">Link</a></li>
		</ul>
	</nav>
	<div class="container" align="center" style="margin-top: 80px">
		<h2>주소 검색</h2>

		<!-- Button to Open the Modal -->
		<div class="input-group mb-3">
			<input type="text" id="bt" class="form-control"
				placeholder="placeholder">
			<div class="input-group-append">
				<button type="button" class="btn btn-success" data-toggle="modal"
					data-target="#myModal">Search</button>
			</div>
		</div>
		<!-- The Modal -->
		<div class="modal" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">주소 검색</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body">
						<table class="table"></table>
					</div>

					<!-- Modal footer -->
					<div class="modal-footer">
						<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
					</div>

				</div>
			</div>
		</div>
	</div>
	<br>
	<hr>
	<br>
	<div class="container" align="center">
		<form method='post' id="form" action="sendAddr.do">
			<div class="list">
				<table id="table_part" class="table">
					<!-- 세션 처리 위한 코드 07.24 김재성 08.05수정-->
					<c:if test="${sessionScope.startPlaceList != null}">
						<c:forEach var="startPlace" items="${sessionScope.startPlaceList}"
							varStatus="status">
							<tr>
								<td width="75%">${startPlace.name}<br>
									${startPlace.address} 
									<div class='place_values'>
									<input type='hidden' name='name' value='${startPlace.name}' /> 
									<input type='hidden' name='address' value='${startPlace.address}' />
									<input type='hidden' name='x' value='${startPlace.x}' /> 
									<input type='hidden' name='y' value='${startPlace.y}' />
									</div></td>
								<td width="20%" align="right"><input type='button'
									class='btn btn-outline-danger btn-sm'
									onclick='remove_addr(this)' value='삭제'></td>
							</tr>
						</c:forEach>
					</c:if>
				</table>
			</div>
			<br> <input id="check_data" class="btn btn-block btn-info"
				type='submit' value='전송'>
		</form>
	</div>
	<br><br>
	<div id="login">
		
	</div>
</body>
</html>









