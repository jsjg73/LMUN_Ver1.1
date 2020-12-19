<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<title>title</title>
<meta charset="UTF-8">
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

#cafelist {
	max-width: 800px;
}

#foodlist {
	max-width: 800px;
}

#culturelist {
	max-width: 800px;
}

#travellist {
	max-width: 800px;
}

.s2 {
	line-height: 90px;
	text-align: center;
}

/* If the text has multiple lines, add the following: */
.s2 i {
	line-height: 1.5;
	vertical-align: middle;
}

.col {
	padding: 10px;
}
</style>
<script defer src="<%=request.getContextPath()%>/js/all.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="<%=request.getContextPath()%>/js/friend.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>

<!-- ------------------------------  -->
<script>
	

	$(document).ready(function() {
		$(".categorylist").css("display", "none");
		$("#cafelist").css("display", "");

		$('.col').click(function() {

			//보여줄 태그 선택
			var target = $(this).find("h5").text().toLowerCase() + "list";
			$(".categorylist").css("display", "none"); // 전부 안보이게
			$("#" + target).css("display", ""); // 보여줄 것만 display 설정
		});
	});
	
	var doubleSubmitFlag = false;
    function doubleSubmitCheck(){
        if(doubleSubmitFlag){
            return doubleSubmitFlag;
        }else{
            doubleSubmitFlag = true;
            return false;
        }
    }
	
	function sendLink(btn) {
		if(doubleSubmitCheck()) return;
		
		var url = $(btn).data("url");
		var id = $(btn).data("id");
	    var name = $(btn).data("name");
	    var x = $(btn).data("x");
	    var y = $(btn).data("y");
	    var address = $(btn).data("address");
	    var phone = $(btn).data("phone");
	    //status==1 :: 정상접근확인용. 링크로 route.jsp 링크 접속시 status==null
	    location.href="route.do?status=1&name="+name+
	    		"&id="+id+
	    		"&x="+x+
	    		"&y="+y+
	    		"&address="+address+
	    		"&phone="+phone+
	    		"&place_url="+url;
	    //$(btn).attr("disabled", true);
	  }
</script>

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
											<div class="w3-col s8">
												<span class="w3-bar-item ontop">${fr_push.inviter}</span>
											</div>
											<div class="w3-col s2">
												<button class="w3-bar-item w3-button ontop" onclick="frpush('${fr_push.inviter}','1')">O</button>
											</div>
											<div class="w3-col s2">
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
	<div class="container-sm" align="center" style="margin-top: 80px">
		<!-- 
		<div class="container">
			<img src="map.png" style="width:100%">
		</div>
		 -->
		<br>
		<div class="row" style="max-width: 800px;">
			<div class="col w3-button">
				<i class="fa fa-coffee fa-3x"></i>
				<h5>Cafe</h5>
			</div>
			<div class="col w3-button">
				<i class="fas fa-utensils fa-3x"></i>
				<h5>Food</h5>
			</div>
			<div class="col w3-button">
				<i class="fa fa-film fa-3x"></i>
				<h5>Culture</h5>
			</div>
			<div class="col w3-button">
				<i class="fas fa-archway fa-3x"></i>
				<h5>Travel</h5>
			</div>
		</div>
		<br>
		<div id="cafelist" class="categorylist">
			<ul class="list-group">
				<c:forEach var="item" items="${ce7placeList}">
					<li class="list-group-item">
						<div class="w3-low" style="vertical-align: middle">
							<div class="w3-col s2">
								<i class="fa fa-coffee fa-2x"></i>
							</div>
							<div class="w3-col s8" align="left">
								<h5>
									<b>${item.name}</b>
								</h5>
								<p class="w3-opacity">
									${item.address}<br>${item.phone}<br>
									<a href="${item.place_url}">상세정보</a>
								</p>
							</div>
							<div class="w3-col s2">
								<button class="btn btn-outline-dark" data-name="${item.name}"
									data-x="${item.x}" data-y="${item.y}"
									data-address="${item.address}" data-phone="${item.phone}"
									data-id="${item.id}"
									data-url="${item.place_url}" onclick="sendLink(this)">경로보기</button>
							</div>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div id="foodlist" class="categorylist">
			<ul class="list-group">
				<c:forEach var="item" items="${fd6placeList}">
					<li class="list-group-item">
						<div class="w3-low" style="vertical-align: middle">
							<div class="w3-col s2">
								<i class="fas fa-utensils fa-2x"></i>
							</div>
							<div class="w3-col s8" align="left">
								<h5>
									<b>${item.name}</b>
								</h5>
								<p class="w3-opacity">
									${item.address}<br> ${item.phone}<br>
									<a href="${item.place_url}">상세정보</a>
								</p>
							</div>
							<div class="w3-col s2">
								<button class="btn btn-outline-dark" data-name="${item.name}"
									data-x="${item.x}" data-y="${item.y}"
									data-address="${item.address}" data-phone="${item.phone}"
									data-id="${item.id}"
									data-url="${item.place_url}" onclick="sendLink(this)">경로보기</button>
							</div>


						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div id="culturelist" class="categorylist">
			<ul class="list-group">
				<c:forEach var="item" items="${ct1placeList}">
					<li class="list-group-item">
						<div class="w3-low" style="vertical-align: middle">
							<div class="w3-col s2">
								<i class="fa fa-film fa-2x"></i>
							</div>
							<div class="w3-col s8" align="left">
								<h5>
									<b>${item.name}</b>
								</h5>
								<p class="w3-opacity">
									${item.address}<br> ${item.phone}<br>
									<a href="${item.place_url}">상세정보</a>
								</p>
							</div>
							<div class="w3-col s2">
								<button class="btn btn-outline-dark" data-name="${item.name}"
									data-x="${item.x}" data-y="${item.y}"
									data-address="${item.address}" data-phone="${item.phone}"
									data-id="${item.id}"
									data-url="${item.place_url}" onclick="sendLink(this)">경로보기</button>
							</div>


						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div id="travellist" class="categorylist">
			<ul class="list-group">
				<c:forEach var="item" items="${at4placeList}">
					<li class="list-group-item">
						<div class="w3-low" style="vertical-align: middle">
							<div class="w3-col s2">
								<i class="fas fa-archway fa-2x"></i>
							</div>
							<div class="w3-col s8" align="left">
								<h5>
									<b>${item.name}</b>
								</h5>
								<p class="w3-opacity">
									${item.address}<br> ${item.phone}<br>
									<a href="${item.place_url}">상세정보</a>
								</p>
							</div>
							<div class="w3-col s2">
								<button class="btn btn-outline-dark" data-name="${item.name}"
									data-x="${item.x}" data-y="${item.y}"
									data-address="${item.address}" data-phone="${item.phone}"
									data-id="${item.id}"
									data-url="${item.place_url}" onclick="sendLink(this)">경로보기</button>
							</div>


						</div>
					</li>
				</c:forEach>
			</ul>
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
</body>
</html>



