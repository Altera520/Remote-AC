<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
		var httpRequest = new XMLHttpRequest();
		httpRequest.onreadystatechange = callback;
		function check_id() {
			var item=document.getElementById('user_id').value;
			httpRequest.open("GET", "checkID.Action"+"?userName="+item, true);
			httpRequest.send();	
		}

		function check_psw() {

		}

		function callback() {
			if (httpRequest.readyState == 4) {
				if (httpRequest.status == 200) {
					if(httpRequest.responseText=="true"){
						document.getElementById('id_msg').innerHTML = "존재하는 아이디 입니다";
						document.getElementById('submit_btn').disabled='disabled';
					}
					else{
						document.getElementById('id_msg').innerHTML = "사용가능한 아이디 입니다";
						document.getElementById('submit_btn').disabled=false;
					}
				}
			}
		}
	</script>
	<form action="memberJoin.Action" method="post">
		아이디: <input type="text" name="id" required onkeyup="check_id();" id="user_id"><br>
		<div id="id_msg"></div>
		패스워드: <input type="password" name="psw" required><br>
		패스워드 확인: <input type="password" name="psw_sign" required><br>
		<div id="id_psw"></div>
		<input type="submit" name="회원가입" id="submit_btn">
	</form>
</body>
</html>