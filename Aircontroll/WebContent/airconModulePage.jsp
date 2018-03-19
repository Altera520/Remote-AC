<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- <link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/c3/0.4.18/c3.min.css"> -->
<!-- <link rel="stylesheet" href="style/c3chart.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/c3/0.4.11/c3.min.js"></script>
<script src="https://d3js.org/d3.v3.min.js"></script> -->
<!-- <script type="text/javascript" src="MainPage/c3charts.js"></script> -->
<link href="https://fonts.googleapis.com/css?family=Exo"
	rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Rajdhani"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="https://cdn.rawgit.com/moonspam/NanumSquare/master/nanumsquare.css">
<link href="Resources/air_module.css?ver=4" rel="stylesheet">
<script type="text/javascript" src="Scripts/irsend.js?ver=6"></script>
<script type="text/javascript" src="Scripts/highstock/highstock.js"></script>
<script type="text/javascript" src="Scripts/highstock/dark-unica.js"></script>
<script type="text/javascript">
	<c:if test="${TempHumiJson ne null}">
	var dataset = ${TempHumiJson};
	</c:if>
	var AC_state = '${sessionScope.ACState.state}';
</script>
</head>

<body>
	<c:set value="현재 온도를 표시할 수 없습니다." var="temp"></c:set>
	<c:set value="현재 습도를 표시할 수 없습니다." var="humi"></c:set>
	<h1 class="aircon_title">에어컨 제어 모듈</h1>
	<div id="air_container" style="font-family: 'NanumSquare', sans-serif;">
		<div id="air_main">
			<div class="page">
				<span class="page_title"><strong>온습도 데이터 모니터링</strong></span>
				<div class="page_monitor">
				<c:choose>
						<c:when test="${TempHumiJson eq null}">
							<span style="text-align:center; font-size:20px;
				color:white; display:block; margin-top:15px; background-color:rgb(101,101,101);
				padding:5px; border-radius: 10px; margin-left:100px;margin-right:100px;
				margin-bottom:15px;
				">
				데이터가 존재하지 않습니다.
				</span>
				</c:when>
				<c:otherwise>
					<div class="air_dataset">
						<div class="latest_data">
							Temperature
							<hr style="width: 100%;">
							<span class="text" id="temp"><c:out
									value="${requestScope.LatestHumiTempDTO.temp}°C"
									default="0"></c:out></span>
						</div>
						<div class="latest_data">
							Humidity
							<hr style="width: 100%">
							<span class="text" id="humi"><c:out
									value="${requestScope.LatestHumiTempDTO.humi}%rh"
									default="0"></c:out></span>
						</div>
						<div class="latest_data">
							State
							<hr style="width: 100%;">
							<span class="state" id="feel">good:)</span>
						</div>
						<div class="latest_data">
							Timestamp
							<hr style="width: 100%;">
							<span class="text" id="time">
							<c:out
									value="${requestScope.LatestHumiTempDTO.dateFormat}"
									default="0"></c:out>
							</span>
						</div>
					</div>
					<div id="chart_temp"></div>
						<div id="chart_humi"></div>
						<script src="Scripts/highstock/highstock.call_func.js?ver=1"></script>
					</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="page monitor">
			<span class="page_title"><strong>에어컨 원격 제어</strong></span> <span
				class="page_title_sub">에어컨 제어</span>
			<div class="page_monitor">
				<div class="air_dataset sub">
					<img alt="" src="Resources/AC.png" width="100" height="100" id="AC">
					<div class="latest_data" style="margin-left: 5">
						Temperature
						<hr style="width: 100%;">
						<span class="text"> <c:out
								value="${ACState.temp}°C"></c:out>
						</span>
					</div>
					<div class="latest_data">
						company
						<hr style="width: 100%">
						<span class="text">LG</span>
					</div>
					<div class="latest_data">
						protocol
						<hr style="width: 100%;">
						<span class="text">NEC</span>
					</div>
					<div class="latest_data">
						current State
						<hr style="width: 100%;">
						<span class="state" id="AC_state"></span>
					</div>
				</div>
			</div>
			<hr>
			<div class="air_dataset sub down">
				<div class="air_button_box">
					<button class="air_button" id="on_btn" onclick="ir_on();">켜기</button>
				</div>
				<div class="air_button_box">
					<button class="air_button" id="off_btn" onclick="ir_off();">끄기</button>
				</div>
				<div class="air_button_box">
					<button class="air_button" id="up">온도 증가</button>
				</div>
				<div class="air_button_box">
					<button class="air_button" id="down">온도 감소</button>
				</div>
				<div class="air_button_box">
					<button class="air_button" id="fast">팬 가속</button>
				</div>
			</div>
			<script type="text/javascript">
				if (AC_state=='on')
					set_btn(true, false);
				else
					set_btn(false, true);
			</script>
			<!-- <span class="page_title_sub">최근 사용 기록</span>
			<div class="page_monitor">
				<div class="air_dataset sub">
					<div id="ir_message">에어컨이 꺼져있습니다.</div>
				</div>
			</div> -->
			<!-- <span class="page_title latest"><strong>최근 수행 동작</strong></span> -->
			<!-- <div class="latest_log">
				
			</div> -->
		</div>
		<div class="page monitor">
			<span class="page_title"><strong><%=Calendar.MONTH%>월<%=Calendar.DAY_OF_MONTH%>일의 에어컨 사용 기록</strong></span>
			<div class="table_height">
			<table id="tab">
				<thead class="table_header">
					<tr>
						<th>NO.</th>
						<th>수행동작</th>
						<th>수행IP</th>
						<th>수행시간</th>
						<th>수행결과</th>
					</tr>
				</thead>
				<tbody id="log">
				<c:set value="성공" var="result"></c:set>
				<c:forEach items="${dtosLog}" var="log" varStatus="status" begin="1">
					<tr>
						<td>${status.count}</td>
						<td>${log.act}</td>
						<td>${log.ip}</td>
						<td>${log.date}</td>
						<c:choose>
							<c:when test="${log.result eq result}">
								<td><span class="state result" id="result">${log.result}</span></td>
							</c:when>
							<c:otherwise>
								<td><span class="state result fail" id="result">${log.result}</span></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<c:if test="${dtosLog eq null}">
				<span id="log_text" style="text-align:center; font-size:20px;
				color:white; display:block; margin-top:200px; background-color:rgb(101,101,101);
				margin-left:50px;margin-right:50px; padding:5px; border-radius: 10px;
				">
				사용 기록이 존재하지 않습니다.
				</span>
			</c:if>
			</div>
		</div>
	</div>
</body>

</html>