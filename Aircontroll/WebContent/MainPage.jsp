<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Smart Home</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Exo"
	rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Rajdhani"
	rel="stylesheet">
<link rel="stylesheet" href="Resources/mainPage_style.css">
<script type="text/javascript" src="http://jsgetip.appspot.com"></script>
<script src="https://code.jquery.com/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="Scripts/spin.js"></script>
<script type="text/javascript" src="Scripts/mainpage_action.js"></script>
<script>
	setTimeout('expireSession()',<%=request.getSession(false).getMaxInactiveInterval()*1000%>);
	function expireSession() {
		window.location = "logout.Action";
	}
</script>
</head>
<body onload="loadDate()">
	<c:if test="${sessionScope.loginInfo eq null}">
		<jsp:forward page="LoginFail.Action"></jsp:forward>
	</c:if>
	<div id="container">
		<!-- header -->
		<div class="header">
			<div class="header side">
				<h1 class="main_title">Smart Home</h1>
			</div>
			<strong><span id="clock" class="main_span"></span></strong>
			<button class="logout" onclick="logout();">로그아웃</button>
		</div>
		<!-- body -->
		<div id="content">
			<!-- sidebar -->
			<div class="sidebar header">
				<div class="profile">
					<img src="Resources/Homeicon.png" alt="" width="70">
				</div>
				<h2>${sessionScope.loginInfo.userName}</h2>
			</div>
			<div class="sidebar body">
				<span class="main_span">AP's ip address</span> <span class="main_span ipaddr"><strong>203.250.32.126</strong></span>
				<span class="main_span">current pc's ip address</span> <span class="main_span ipaddr"> <strong>
						<script type="text/javascript">
							document.write(ip()); // IP Address
						</script>
				</strong>
				</span>
				<h2 class="border"></h2>
				<span class="main_span">Your Modules</span>
				<ul class="nanumgothic">
					<li><a onclick="changePage('airconModulePage.jsp');">에어컨 제어 모듈</a></li>
					<li><a onclick="changePage('gasMonitoringModule');">가스누수
							모듈</a></li>
				</ul>
				<h2 class="border"></h2>
			</div>
			<!-- main content -->
			<div id="main">
				<jsp:include page="airconModulePage.jsp"></jsp:include>
			</div>
			<!-- another item -->
			<div id="ad"></div>
		</div>
		<!-- footer -->
		<div id="footer"></div>
	</div>
</body>
</html>