/*
 Name:		IR_slave.ino
 Created:	2018-02-12 오후 10:43:24
 Author:	AN
*/
#include <ArduinoJson.h>
#include <SoftwareSerial.h>
#include <DHT11.h>
char* userName = "";
char* ssid = ""; //AP's ssid
char* psw = ""; // AP's password

char* server_ip = "203.250.32.27"; //웹 서버 아이피
unsigned int server_port = 9001; //웹 서버 포트
SoftwareSerial client = SoftwareSerial(10, 11); //esp8266모듈 핀 번호
String uri = "/Aircontroll/setHumiAndTemp.Action";

String bluetooth_cmd = "";
/*------------related DHT11 sensor------------*/
//DHT pin6
long check_prev;
long prev, time_interval = 12000;//12000;
const int DHT_pin = 6;
DHT11 dht11(DHT_pin);

int temp_avr = 0;
int humi_avr = 0;
int counter = 0;
boolean wifi_join = false;

void esp8266Client_setup() {
	Serial.begin(9600);
	client.begin(9600);
	delay(1000);
	sendData("AT+RST\r\n", 3000, 0);
	sendData("AT\r\n", 2000, 0);
	sendData("AT+CWMODE=1\r\n", 2000, 0);
	check_prev = millis();
}

void esp8266_joinAP() {
	sendData("AT+CWQAP\r\n", 2000, 0);
	String join = String("AT+CWJAP=\"") + ssid + "\",\"" + psw + "\"\r\n";
	sendData(join, 5000, 0);
	if (sendData("AT+CWJAP?\r\n", 3000, 0).indexOf("OK") != -1) {
		Serial.print('1');//송신 아두이노에게 성공함을 전송
		sendData("AT+CIFSR\r\n", 2000, 0);
		wifi_join = true;
		//AP나 서버로 현재 할당받은 내부 아이피 알려줘야 함
	}
	else {
		Serial.print('0');//송신 아두이노에게 실패함을 전송
	}
}

void esp8266_send(String temp, String humi) {
	String conn = String("AT+CIPSTART=\"TCP\"") + ",\"" + server_ip + "\"," + server_port + "\r\n";
	if (sendData(conn, 3000, 0).indexOf("OK") == -1) {
		return;
	}
	String query = "?userName=" + String(userName) + "&temp=" + temp + "&humi=" + humi;
	String request = "GET " + uri + query + "\r\n";
	request += "Connection:close\r\n\r\n";
	sendData(String("AT+CIPSEND=") + request.length() + "\r\n", 1000, 0);
	sendData(request, 1000, 0);
	client.flush();
}

void dht11_setup() {
	prev = millis();
}

void dht11_read() {
	long current = millis();
	if (current - prev > time_interval) {
		int err = 1;
		float temperature = -1, humidity = -1;
		if ((err = dht11.read(humidity, temperature)) == 0) {
			counter++;
			temp_avr += temperature;
			humi_avr += humidity;
			if (counter == 5) {
				esp8266_send(String(temp_avr / 5), String(humi_avr / 5)); //send
				counter = 0;
				temp_avr = 0;
				humi_avr = 0;
			}
		}
		prev = millis();
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

String sendData(String command, long timeout, boolean debug) {
	String response = "";
	client.print(command);
	long time = millis();
	while ((time + timeout) > millis()) {
		while (client.available()) {
			char c = client.read();
			response += c;
		}
	}
	if (!debug) {
		//Serial.println(response);
	}
	return response;
}

// the setup function runs once when you press reset or power the board
void setup() {
	esp8266Client_setup();
	while (!Serial.available()){}//AVR스핀
	delay(2000);
	while (Serial.available())
	{
		bluetooth_cmd += (char)Serial.read();
	}
	//Serial.println(bluetooth_cmd);
	bluetooth_set();
	esp8266_joinAP();
	dht11_setup();
}

// the loop function runs over and over again until power down or reset
void loop() {
	if (wifi_join) {
		dht11_read();
	}
}
