<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:if test="${result == 1}">
	<script>
		alert("회원 정보 없음");
		history.go(-1);
	</script>
</c:if>   

<c:if test="${result == 2}">
	<script>
		alert("비밀번호 불일치");
		history.go(-1);
	</script>
</c:if>  

<c:if test="${result == 3}">
	<script>
		alert("세션 없음");
		location.href="member_login.do";
	</script>
</c:if> 