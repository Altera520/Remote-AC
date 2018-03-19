/*
 Name:		IR_master.ino
 Created:	2018-02-12 오후 7:35:00
 Author:	AN
*/
#include <ArduinoJson.h>
#include <ir_Lego_PF_BitStreamEncoder.h>
#include <IRremoteInt.h>
#include <IRremote.h>
#include <boarddefs.h>
#include <SoftwareSerial.h>
char* userName = "";
char* ssid = ""; //AP's ssid
char* psw = ""; // AP's password
char* server_ip = "";
SoftwareSerial server = SoftwareSerial(10, 11);
SoftwareSerial bluetooth = SoftwareSerial(4, 5);

boolean bluetooth_protocol = false; // '{' is true, '}' is false
boolean wifi_join = false;
String bluetooth_cmd = "";

String uri = "/Aircontroll/setHumiAndTemp.Action";
String buffer = "";
int buffer_count = 0;
long check_prev;

//const int piezo = 12;
const int success_led = 7;
const int fail_led = 8;

/*------------related IR LED setting------------*/

//D3
IRsend IRLED = IRsend();
int ir_protocol = 0x616A817E;
int ir_protocol_length = 32;
const int LED = 9;
byte IsAirconOn = false;

void esp8266Server_setup() {
	Serial.begin(9600);
	server.begin(9600);
	delay(1000);
	sendData("AT+RST\r\n", 3000, 0);
	sendData("AT\r\n", 2000, 0);
	sendData("AT+CWMODE=1\r\n", 2000, 0);
	sendData("AT+CIPMUX=1\r\n", 2000, 0);
	sendData("AT+CIPSERVER=1,80\r\n", 2000, 0);
}

String sendData(String command, long timeout, boolean debug) {
	String response = "";
	server.print(command);
	long time = millis();
	while ((time + timeout) > millis()) {
		while (server.available()) {
			char c = server.read();
			response += c;
		}
	}
	if (!debug) {
		//Serial.println(response);
	}
	return response;
}

void esp8266_joinAP() {
	change_led_state(0);//빨간불
	sendData("AT+CWQAP\r\n", 2000, 0);
	String join = String("AT+CWJAP=\"") + ssid + "\",\"" + psw + "\"\r\n";
	sendData(join, 5000, 0);
	if (sendData("AT+CWJAP?\r\n", 3000, 0).indexOf("OK") != -1) {
		Serial.print(bluetooth_cmd);
		while (!Serial.available()){}
		delay(500);
		while (Serial.available())
		{
			char res = (char)Serial.read();
			if (res!='1') {
				return;
			}
		}
		sendData("AT+CIFSR\r\n", 2000, 0);
		change_led_state(1);//파란불
								//tone(piezo, 392);//AP연결 설정이 끝났으므로 소리로 알려준다.
								//delay(1000);
								//noTone(piezo);
		wifi_join = true;
		
		//AP나 서버로 현재 할당받은 내부 아이피 알려줘야 함
	}
	else {
		change_led_state(0);//빨간불
	}
}

void change_led_state(boolean st) {
	if (st == 0) {//빨강
		digitalWrite(success_led, LOW);
		digitalWrite(fail_led, HIGH);
	}
	else {//파랑
		digitalWrite(success_led, HIGH);
		digitalWrite(fail_led, LOW);
	}
}
void ir_send() {
	IRLED.sendNEC(0x616A817E, 32);
	//irsend.sendNEC(ir_protocol, ir_protocol_length);
	IsAirconOn = !IsAirconOn;//조도 센서를 사용해서 에어컨 켜진지 판단해야
	if (IsAirconOn) {
		digitalWrite(LED, HIGH);
	}
	else {
		digitalWrite(LED, LOW);
	}
}

#if 1
void esp8266_read() { //명령 라우팅
	if (server.available()) {
		String temp = server.readStringUntil('\n');
		Serial.println("DEBUG: " + temp);
		buffer += temp;
		if (temp.charAt(0) == 13) {
			buffer_count++;
			if (buffer_count == 2) {//\r\n\r\n까지 받아오면 수행
				buffer_count = 0;
				unsigned int c_id = buffer.charAt(buffer.indexOf("+IPD,") + 5) - 48;
				Serial.println(c_id);
				int cmd = buffer.charAt(buffer.indexOf("cmd=") + 4) - 48;
				Serial.println(cmd);
				String content = "";
				switch (cmd) //cmd 라우팅
				{
				case 1:
					ir_send();
					content = "on";
					Serial.println(content);
					break;
				case 2:
					ir_send();
					content = "off";
					Serial.println(content);
					break;
				default:
					content = "err";
					break;
				}
				buffer = ""; //String 버퍼 클리어

				String response = "HTTP/1.1 200 OK\r\n";
				response += "Content-Type:text/html;charset=UTF-8\r\n";//CORS 핸들링에  따른 content-type만 가능
				response += "Content-Length:";
				response += content.length();
				response += "\r\n";
				response += "Access-Control-Allow-Origin:*\r\n";//CORS
				response += "Connection:close\r\n\r\n";//포함하지 않으면 esp 혼동
													   //response += "Keep-Alive:timeout=5,max=999\r\n\r\n";
				response += content;
				sendData(String("AT+CIPSEND=") + c_id + "," + response.length() + "\r\n", 3000, 0);
				sendData(response, 1000, 1);
				server.flush();
			}
		}
		else buffer_count = 0;
	}
}
#endif

void bluetooth_setup() {
	bluetooth.begin(9600);
	bluetooth_write("AT+NAMEaircon", 13); //블루투스 이름은 aircon
	bluetooth_write("AT+PIN2014", 10); //bluetooth psw is 2014
}

void bluetooth_read() {
	while (!bluetooth.available()){}
	delay(2000);
	while (bluetooth.available())
	{
		char temp = (char)bluetooth.read();
		if (temp == '{') {// 1. 첫번째 문자가 '{'라면 블루투스 통신시작
			bluetooth_protocol = true;
		}
		else if (bluetooth_protocol && temp == '}') {// 3. 끝이 '}'라면 통신끝
			bluetooth_protocol = false;
			bluetooth_cmd += temp;

			bluetooth_set(); //bluetooth_cmd에 저장된 정보를 읽어온다(userName,ssid,psw)
			server.listen();
			esp8266_joinAP();//esp랑 AP연결
			bluetooth_cmd = "";//문자열을 비운다
		}
		if (bluetooth_protocol) {// 2. 블루투스 통신중이라면
			bluetooth_cmd += temp;
		}
	}
}

void bluetooth_set() {
	StaticJsonBuffer<100> jsonBuffer;
	JsonObject& jsonValue = jsonBuffer.parseObject(bluetooth_cmd);//json포맷으로 읽어온다
																  //{userName:"~",ssid:"~",psw:"~"}
	userName = jsonValue["userName"];//사용자 아이디
	ssid = jsonValue["ssid"];//AP이름
	psw = jsonValue["psw"];//AP패스워드
}

void bluetooth_write(char* cmd, size_t len) {	
	bluetooth.listen();
	for (size_t i = 0; i < len; i++)
	{
		bluetooth.write(cmd[i]);
	}
	delay(2000);
	while (bluetooth.available()) bluetooth.read();
}


// the setup function runs once when you press reset or power the board
void setup() {
	pinMode(LED, OUTPUT);
	pinMode(success_led, OUTPUT);
	pinMode(fail_led, OUTPUT); //점멸등
	digitalWrite(success_led, LOW);
	digitalWrite(fail_led, LOW);
	server.listen();
	esp8266Server_setup();
	bluetooth_setup();
	change_led_state(0);//빨간불
	//Serial.println("ready");
	bluetooth_read();
	//Serial.println("start");
}

// the loop function runs over and over again until power down or reset
void loop() {
	if(wifi_join)
		esp8266_read();
}
