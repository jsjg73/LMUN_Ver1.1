<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<title>우리지금만나(Let'sMeetNow)</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap"
	rel="stylesheet">

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
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0b6c81e5d496e486ca93f4d82d0a0027&libraries=services"></script>
<script src="<%=request.getContextPath()%>/js/home.js"></script>
<script src="<%=request.getContextPath()%>/js/all.min.js"></script>
<script>
	window.onpageshow = function(event) {
		if (event.persisted
				|| (window.performance && window.performance.navigation.type == 2)) {
			// Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
			console.log("뒤로가기로 들어왔습니다.");
			location.reload();
		} else {
			console.log("새로 진입123");
		}
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
					<div class="w3-dropdown-hover" id="fr-accept">
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
												<button class="w3-bar-item ontop" onclick="frpush('${fr_push.inviter}','1')">o</button>
											</div>
											<div class="w3-col s2">
												<button class="w3-bar-item ontop" onclick="frpush('${fr_push.inviter}','2')">x</button>
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
		<h3>출발지 목록</h3>

		<!-- Button to Open the Modal -->
		<div class="input-group mb-3">
			<input type="text" id="search_bar" class="form-control"
				placeholder="input your address">
			<div class="input-group-append">
				<button type="button" id="search" class="btn btn-dark"
					data-toggle="modal" data-target="#myModal">검색</button>
				<br>
			</div>

		</div>
		<c:if test="${not empty email}">
			<div align="left">
				<button type="button" id="friend-list" class="btn btn-dark"
					data-toggle="modal" data-target="#friend-Modal">친구 목록</button>
			</div>
		</c:if>
		<div align="center">
			우리의 중간은 어디일까?<br> 공평한 약속 장소를 찾을 수 있드록 도와드립니다.<br> 참석자들의
			출발지를 입력하시면 장소를 추천해드려요.<br> 최대 10인.
		</div>
		<!-- The Modal -->
		<div class="modal" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">
							<b>&nbsp;&nbsp;Result</b>
						</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body" style='font-size: 18px'>
						<table id="table_part" class="w3-table w3-bordered w3-hoverable">
							<tbody wdith="100%">

							</tbody>
						</table>
					</div>


				</div>
			</div>
		</div>

		<!-- The Friend-Modal -->
		<div class="modal" id="friend-Modal">
			<div class="modal-dialog">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">
							<b>&nbsp;&nbsp; 친구 목록</b>
						</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body" style='font-size: 18px'>
						<table id="fr-table_part"
							class="w3-table w3-bordered w3-hoverable">
							<tbody wdith="100%">
								<c:forEach var="item" items="${fr_list}">
									<tr>
										<td><div align="center">${item.nickname}
												<span class=w3-opacity style='font-size: 12px'>${item.email}</span>
											</div></td><td>
											<div align="center">
												<button type="button" data-nickname="${item.nickname}"
													data-addr="${item.addr1}" data-x_="${item.x_}"
													data-y_="${item.y_}" class="btn btn-dark"
													onclick="fr_down(this)">주소 1</button>
												<!-- c:if -->
												<%-- <button data-nickname="${item.nickname}"
													data-addr="${item.addr2}" data-x_="${item.x_}"
													data-y_="${item.y_}" type="button" class="btn btn-success"
													onclick="fr_down(this)">주소 2</button>
												<button data-nickname="${item.nickname}"
													data-addr="${item.addr3}" data-x_="${item.x_}"
													data-y_="${item.y_}" type="button" class="btn btn-success"
													onclick="fr_down(this)">주소 3</button> --%>
											</div></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

				</div>
			</div>
		</div>
	</div>
	<br>
	<hr>
	<br>
	<form id="form" action="sendAddr2.do">
		<div class="container-sm">

			<ul class="w3-ul w3-card-4">
				<c:forEach items="${startPlaceList}" var="item">
					<li class="w3-bar"><span onclick="remove(this)"
						data-x='${item.x}'
						class="w3-bar-item w3-button w3-white w3-xlarge w3-right">×</span>
						<div class="w3-bar-item">
							<span class="w3-large">${item.name} </span><br> <span
								class="w3-addr"> ${item.address}</span>
						</div></li>

				</c:forEach>
			</ul>
			<div align="center">
				<br>
				<button id="check_data" type="submit" class="btn btn-dark">중간지점 찾기</button>
			</div>

		</div>
		<c:forEach items="${startPlaceList}" var="item">
			<div class='place_values'>
				<input type='hidden' name='name' value='${item.name}' /> <input
					type='hidden' name='x' value='${item.x}' /> <input type='hidden'
					name='address' value='${item.address}' /> <input type='hidden'
					name='y' value='${item.y}'>
			</div>
		</c:forEach>
	</form>
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
