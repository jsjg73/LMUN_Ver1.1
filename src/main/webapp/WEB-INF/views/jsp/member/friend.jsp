<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<title>title</title>
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
<script src="<%=request.getContextPath()%>/js/all.min.js"></script>
<script src="<%=request.getContextPath()%>/js/friend.js"></script>
<script>

function validate_useremail(val)
{
  var pattern= new RegExp(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i);
    
  //regular expression
  return pattern.test(val);
};

function email_check(){
	/* $("#searchResult").hide(); *///span area
	var email=$("#email").val();
	//verification
	if(!(validate_useremail(email))){
		var newtext='<li class="list-group-item"><font color="red">이메일 형식 오류</font></li>';
		$("#resultList").show();
		$("#resultList").empty();
		$("#resultList").append(newtext);
		$("#email").val("").focus();
		return false;
	};
	

	//email duplicate check
    $.ajax({
        type:"POST",
        url:"friend_search.do",
        data: {"email":email},
        dataType:'text', 
        success: function (data) {
        	console.log(data)
        	if(data == ""){
        		alert("없는 회원입니다.")
       		}else{
       			
               	var obj = JSON.parse(data);	
               	var str = '<li class="list-group-item"><div class="w3-low"><div class="w3-col s10">Email : '
               			+ obj.email +'<br>Nickname : '+ obj.nickname + '</div><div class="w3-col s2 add">'
               			+ '</div></div></li>';
               	$("#resultList").show();
               	$("#resultList").empty();
               	$("#resultList").append(str);
               	
               	
               	var btn = '<span id="add_btn" class="w3-bar-item w3-button w3-white w3-xlarge w3-right">추가하기</span>';
               	$(".add").append(btn);
               	
               	$("#add_btn").attr('onclick', 'add("'+obj.email+'")');
       		}
        	
        }
        ,
    	  error:function(e){
    		  alert("data error"+e);
    	  }
      });//$.ajax
};


function deleteCheck(num){
	console.log(num);
	var check = confirm('Delete Confirm');
	console.log(check);
	if(check){		
		location.href="<%=request.getContextPath()%>/friend_del.do?fno="+num;
	}
}
//친구 삭제 --------------------------------------------------
function delf(btn2){
   var friendEmail = $(btn2).data('x');
   var check = confirm("삭제하시겠습니까?")
   if(check){
	   $.ajax({
	        type:"POST",
	        url:"friend_del.do",
	        data: {"email": friendEmail},
	        dataType:'text',
	        success: function (data) {
	           /* alert("return success="+data); */
	           item = $(btn2).closest('li');
	           item.remove();
	        }
	        ,
	         error:function(e){
	            alert("data error"+e);
	         }
	      });
   }
  
}
//친구 추가 --------------------------------------------------
function add(email){
	console.log(email);
	var check = confirm('친구 추가 하시겠습니까?');
	console.log(check);
	if(check){
		$.ajax({
	        type:"POST",
	        url:"friend_add.do",
	        data: {"email":email},
	        success: function (data) {

	        	if(data==-1){		
	        		alert("이미 등록된 친구")
	        		return false;
	        	} else if(data==-2){
	        		alert("이미 요청을 보낸 친구입니다.")
	        	}else if(data ==-3){
	        		alert("이미 요청 받은 친구입니다.")
	        	}else if(data==100){	
	        		alert("자신을 추가할 수 없습니다.")
	        	}else{
	        		alert("친구 요청을 보냈습니다.")
		      		location.href="<%=request.getContextPath()%>/friendlist.do";
							}
						},
						error : function(e) {
							alert("data error" + e);
						}
					});//$.ajax
		}
	}

	$(document).ready(function() {
		$('#resultList').hide();
	});
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
								<c:forEach var="fr_push" items="${fr_push}" varStatus="st">
									<div class="w3-bar-item fr_push_list">
										<div class="w3-row" >
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
	<div class='container-sm' style='max-width: 500px; margin-top: 80px'
		align="left">

		<h1>Friend List</h1>

		<br>
		<ul class="list-group">
			<c:forEach var="friend" items="${list}" varStatus="status">
				<li class="list-group-item">
					<div class="w3-low">
						<div class="w3-col s10">

							Email : ${friend.email }<br>addr : ${friend.addr1}
						</div>
						<div class="w3-col s2">
							<span data-x="${friend.email}" onclick="delf(this)"
								class="w3-bar-item w3-button w3-white w3-xlarge w3-right">x</span>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
		<br> <br>
		<div class="w3-row">
			<div class="w3-col w3-center">
				<form>
					<input type="text" id="email">
					<button type="button" class="btn btn-outline-dark"
						onclick="email_check()">친구 찾기</button>
					<button type="button" class="btn btn-outline-dark" data-toggle="modal"
						data-target="#myModal"><i class="fas fa-exclamation"></i>&ensp;<span class="badge badge-dark">${fn:length(fr_recommend) + fn:length(invite)}</span></button>
				</form>
			</div>
		</div>
	</div>
	<div id="searchResult" class="container-sm"
		style='max-width: 500px; margin-top: 20px'>
		<ul id="resultList" class="list-group">

		</ul>
	</div>
	

	<!-- The Modal -->
	<div class="modal" id="myModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">detail</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<!-- Modal body -->
				<div class="modal-body">
				<h6>수락 혹은 거절시 요청 목록에서 사라집니다.</h6>
					<c:if test="${empty invite}">
						대기중인 친구신청 없음
					</c:if>
					<c:if test="${not empty invite}">
						
						<c:forEach var="invi" items="${invite}" varStatus="status">
							${invi.invitee}<br>
						</c:forEach>
					</c:if>
					<%-- <hr>
					<c:if test="${empty fr_recommend}">
						추천 친구 없음
					</c:if>
					<c:if test="${not empty fr_recommend}">
						<h5>추천친구 ......</h5>
						<c:forEach var="reco" items="${fr_recommend}" varStatus="status">
							${reco.email1}<br>
						</c:forEach>
					</c:if> --%>
				</div>
			</div>
		</div>
	</div>
	<footer class="w3-center w3-light-grey w3-padding-32">
		Contact Us 
		<div class="btn-group">
			<button onclick="location.href='https://github.com/mynameiseunji/mapmap'" title="github" class='btn'><i class="fab fa-github-square fa-lg"></i></button>		
		</div>
	</footer>
</body>
</html>


