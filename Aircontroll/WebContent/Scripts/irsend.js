var httpRequest = new XMLHttpRequest();
var sendLogAjax = new XMLHttpRequest();
var on = true;
var off = false;
var flag = true;
var timer_id;

var action;
var result;
var com_ip;
var date;
httpRequest.onreadystatechange = get_ir_state;
sendLogAjax.onreadystatechange = sendLogFinish;
httpRequest.onerror = timeoutFunc;

function timeoutFunc() {
	set_btn(on, off);
	httpRequest.abort();
	sendLog('err');
}

function ir_on() {
	action = '켜기';
	block_btn();
	httpRequest.open("GET", "irSend.Action?cmd=1", true);
	httpRequest.send();
	timer_id = setTimeout(timeoutFunc, 10000);
}

function ir_off() {
	action = '끄기';
	block_btn();
	httpRequest.open("GET", "irSend.Action?cmd=2", true);
	httpRequest.send();
	timer_id = setTimeout(timeoutFunc, 10000);
}

function get_ir_state() {
	if (httpRequest.readyState == 4) {
		clearTimeout(timer_id);
		setTimeout(
				function() {
					if (httpRequest.status == 200) {
						var res = httpRequest.responseText;
						if (res != null) {
							if (res == "on") {
								set_btn(true, false);
							} else if (res == "off") {
								set_btn(false, true);
							}
							else
								set_btn(on, off);
							sendLog(res);
						}
					}
				}, 2000);
	}
}

function sendLog(res) {
	if (res == 'err')
		result = '실패';
	else
		result = '성공';
	com_ip = ip();
	var queryString = 'saveLog.Action?result=' + result + '&act=' + action
			+ '&ip=' + com_ip;
	sendLogAjax.open("GET", queryString, true);
	sendLogAjax.send();
}

function sendLogFinish() {
	if (sendLogAjax.readyState == 4 && sendLogAjax.status == 200) {
		var log_text=document.getElementById('log_text');
		if(log_text!=null){
			$('#log_text').remove();
		}
		var cells=$('#tab tbody tr');
		for (var i = 0; i < cells.length; i++) {
			cells[i].cells[0].innerHTML=i+2;
		}
		var tab = document.getElementById('log');
		var row = tab.insertRow(0);
		var cell1 = row.insertCell(0);// 번호
		var cell2 = row.insertCell(1);// 수행동작
		var cell3 = row.insertCell(2);// 수행IP
		var cell4 = row.insertCell(3);// 수행시간
		var cell5 = row.insertCell(4);// 수행결과
		cell1.innerHTML = '1';
		cell2.innerHTML = action;
		cell3.innerHTML = com_ip;
		var x=new Date();
		var mon=dateFormat(x.getMonth()+1);
		var day=dateFormat(x.getDate());
		var hour=dateFormat(x.getHours());
		var min=dateFormat(x.getMinutes());
		date=mon+'-'+day+' '+hour+':'+min;
		cell4.innerHTML = date;
		var result_class;
		if(result=='실패'){
			result_class='state result fail';
		}
		else{
			result_class='state result';
		}
		cell5.innerHTML = '<span id="result" class="'+result_class+'"+>'+result+'</span';
	}
}

function set_btn(on_state, off_state) {
	on=on_state;
	off=off_state;
	set_text(on_state);
	document.getElementById('on_btn').disabled = on_state;
	document.getElementById('off_btn').disabled = off_state;
	if (on_state) { // 켜져 있는 경우
		var dom = [ document.getElementById("off_btn"),
				document.getElementById("up"), document.getElementById("down"),
				document.getElementById("fast") ];
		for (var i = 0; i < dom.length; i++) {
			dom[i].style.border = "1px solid black";
			dom[i].style.color = "black";
			dom[i].style.backgroundColor = "white";
			dom[i].style.cursor = "pointer";
		}
		dom = document.getElementById("on_btn");
		dom.style.border = "1px solid white";
		dom.style.color = "white";
		dom.style.backgroundColor = "rgb(131,131,131)";
		dom.style.cursor = "default";
	} else { // 꺼져 있는경우
		var dom = document.getElementById("on_btn");
		dom.style.border = "1px solid black";
		dom.style.color = "black";
		dom.style.backgroundColor = "white";
		dom.style.cursor = "pointer";
		dom = [ document.getElementById("off_btn"),
				document.getElementById("up"), document.getElementById("down"),
				document.getElementById("fast") ];
		for (var i = 0; i < dom.length; i++) {
			dom[i].style.border = "1px solid white";
			dom[i].style.color = "white";
			dom[i].style.backgroundColor = "rgb(131,131,131)";
			dom[i].style.cursor = "default";
		}
	}

}

function block_btn() {
	/*document.getElementById('ir_message').innerHTML = "명령 실행중..";*/
	/*
	 * on = document.getElementById('on_btn').disabled; off =
	 * document.getElementById('off_btn').disabled;
	 */
	var dom = [ document.getElementById("off_btn"),
			document.getElementById("up"), document.getElementById("down"),
			document.getElementById("fast"), document.getElementById("on_btn") ];
	for (var i = 0; i < dom.length; i++) {
		dom[i].disabled = true;
		dom[i].style.border = "1px solid white";
		dom[i].style.color = "white";
		dom[i].style.backgroundColor = "rgb(131,131,131)";
		dom[i].style.cursor = "default";
	}
}

function set_text(value) {
	var st = document.getElementById("AC_state");
	if (!value) {
		st.style.backgroundColor = 'rgb(222,12,0)';
		st.innerHTML = 'AC OFF';
	} else {
		st.style.backgroundColor = '#8bbc21';
		st.innerHTML = 'AC ON';
	}
}
