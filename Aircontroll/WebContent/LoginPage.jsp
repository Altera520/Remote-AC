<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Smart Home</title>
<!-- <meta content="blendTrans(Duration=0.0)" http-equiv="Page-Enter" />
	<meta content="blendTrans(Duration=0.0)" http-equiv="Page-Exit" /> -->
<link rel="stylesheet" type="text/css"
	href="https://cdn.rawgit.com/moonspam/NanumSquare/master/nanumsquare.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="https://fonts.googleapis.com/css?family=Exo"
	rel="stylesheet">
<link rel="stylesheet" href="Resources/loginpage_style.css">
</head>
<body>
	<c:if test="${sessionScope.loginInfo ne null}">
		<jsp:forward page="loginSuccess.Action"></jsp:forward>
	</c:if>
	<div id="container">
		<div class="header">
			<!-- <img src="Homeicon5.png" alt="" height="120"> -->
			<div class="header_nav">
				<h1>
					<span class="span_color">S</span>mart <span class="span_color">H</span>ome
				</h1>
			</div>
			<div class="header_title">
				<span class="title">스마트홈 관리 페이지</span>
				<p>방문을 환영합니다. 당신의 집을 스마트하게 만들기 위해서 시작해보세요.</p>
			</div>
		</div>
		<div id="content">
			<div id="side"></div>
			<div id="main">
				<div class="login">
					<span class="login_title">로그인</span> <span
						class="login_title right"></span>
				</div>
				<form action="login.Action" method="POST">
					<div id="login_form">
						<div class="login_item">
							<label for="login_id" id="login_element"> ID </label> <input
								type="text" id="login_id" name="id" required autofocus>
							<a href="#">ID찾기</a>
						</div>
						<div class="login_item password">
							<label for="login_psw" id="login_element"> PASSWORD </label> <input
								type="password" id="login_psw" name="psw" required> <a
								href="#">비밀번호 찾기</a>
						</div>
						<button>Log In</button>
					</div>
					<!-- <input type="checkbox" id="auto_login">
                	<label for="auto_login">
                		<span id="auto_login_text">자동 로그인</span>
                	</label> -->
				</form>
				<c:if test="${requestScope.userName ne null}">
					로그인 실패 5회중 ${requestScope.blockCount}회 오류<br>
				</c:if>
				<c:if test="${requestScope.loginFail eq true}">
					로그인 실패<br>
				</c:if>
				<span id="sign">회원가입은 <a href="memberJoinPage.jsp"><strong
						id="sign_link">여기</strong></a>를 눌러주세요.
				</span>
			</div>
			<div id="side"></div>
		</div>
	</div>
	<div id="footer">@2018 All Right Reserved. Catholic Univ of Daegu
		520LAB</div>
</body>
</html>