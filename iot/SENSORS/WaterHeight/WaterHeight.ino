#include <PubSubClient.h>
#include <WiFiEsp.h>
#include <SoftwareSerial.h>

// IR Distance Sensor Pin
int echoPin = 12;
int trigPin = 13;


int Sensor_val; //센서 ADC값 저장 변수
int lcd_pos;
byte i;
#define VOLTS_PER_UNIT .0049F // (.0049 for 10 bit A-D) 
float volts;
float inches;
float cm;

// Message format
unsigned long lastMsg = 0;
unsigned long lastTemp = 0;
#define MSG_BUFFER_SIZE  (20)
char msg[MSG_BUFFER_SIZE];

int distance = 0; // initial Distance


SoftwareSerial espSerial(2, 3); // RX, TX
long int baudRate = 9600;

char ssid[] = "Galaxy A313285";             // network SSID (name)
char pass[] = "12345678";        // network password
int status = WL_IDLE_STATUS;      // the Wifi radio's status
char server[] = "192.168.43.96";    // IP address of the MQTT server(Raspberry Pi)
char topic[] = "test";         // Default topic string
char clientId[] = "Arduino waterHeight";      // Cliwent id: Must be unique on the broker

WiFiEspClient wifi;               // Initialize the Ethernet client object
PubSubClient mqttClient(wifi);    // Initialize the MQTT client

void setup() {
  Serial.begin(115200); // serial baud rate
    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
  // Set baud rate of ESP8266 to 9600 regardless of original setting
  set_esp8266_baud_rate(baudRate);

  // Set baud rate of ESP 8266  
  espSerial.begin(baudRate);
  espSerial.print("AT+UART_CUR=");
  espSerial.print(baudRate);
  espSerial.print(",8,1,0,0\r\n");
  WiFi.init(&espSerial);

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }

  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }

  // you're connected now, so print out the data
  Serial.println("You're connected to the network");

  printWifiStatus();

  // mqtt server port : 1883 (general)
  mqttClient.setServer(server, 1883);
  // set of subscriber
  mqttClient.setCallback(onReceive);
}

void loop() {

  // if not connected mqtt -> try reconnect mqtt server
  if (!mqttClient.connected()) {
    reconnect();
  }
  mqttClient.loop();

  unsigned long now = millis();
  

  // publish interval is 2sec 
  if(now-lastMsg>5000){
    lastMsg=now;

    // Sensor_val = analogRead(Sensor); // 센서저장변수에 아날로그값을 저장
    // volts = (float)Sensor_val * VOLTS_PER_UNIT; // 아날로그값을 volt 단위로 변환
    // cm = 60.495 * pow(volts,-1.1904); // 측정전압에 따른 cm단위 거리 계산
    float duration, distance;
    digitalWrite(trigPin, HIGH);
    delay(10);
    digitalWrite(trigPin, LOW);
    duration = pulseIn(echoPin, HIGH); 
    // HIGH 였을 때 시간(초음파가 보냈다가 다시 들어온 시간)을 가지고 거리를 계산 한다.
    distance = ((float)(340 * duration) / 10000) / 2;  

    distance = 48.5 - distance;
    Serial.print("Distance : ");
    Serial.println(distance);
    distance = 48.5-distance;
    snprintf (msg, MSG_BUFFER_SIZE, "HEIGHT/%d", (int)distance);
    Serial.println(msg);
    mqttClient.publish("Arduino/SENSOR", msg);

  }

}

void printWifiStatus()
{
  // print the SSID of the network you're attached to
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength
  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

void onReceive(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i=0;i<length;i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
}

void reconnect() {
  // Loop until we're reconnected
  while (!mqttClient.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (mqttClient.connect(clientId)) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      mqttClient.publish(topic,"hello world");
      // ... and resubscribe
      mqttClient.subscribe(topic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}



// baud rate setup
void set_esp8266_baud_rate(long int baud_rate){
  long int baud_rate_array[] = {1200,2400,4800,9600,19200,38400,57600,74880,115200,230400};
  int i, j, pause=10;
  String response;

  Serial.println("Setting ESP8266 baud rate...");
  for (j=0; j<5; j++){
    for (int i=0; i<10; i++){
      espSerial.begin(baud_rate_array[i]);
      espSerial.print("AT\r\n");
      delay(pause);
      if (espSerial.available()) {
        response=espSerial.readString();
        response.trim();
        if (response.endsWith("OK")) {
          espSerial.print("AT+UART_CUR=");
          espSerial.print(baud_rate);
          espSerial.println(",8,1,0,0");
          delay(pause);
          if (espSerial.available()) {
            response=espSerial.readString();
          }
          espSerial.begin(baudRate);
          delay(pause);
          espSerial.println("AT");
          delay(pause);
          if (espSerial.available()) {
            response=espSerial.readString();
            response.trim();
            if (response.endsWith("OK"))
              break;
            else {
              Serial.println("Trying again...");
            }
          }
          else {
            Serial.println("Trying again...");
          }
        }
      }
    }
    if (response.endsWith("OK"))
      break;
  }
  espSerial.begin(baudRate);
  delay(pause);
  espSerial.println("AT");
  delay(pause);
  if (espSerial.available()) {
    response=espSerial.readString();
    response.trim();
    if (response.endsWith("OK")) {
      Serial.print("\r\nBaud rate is now ");
      Serial.println(baudRate);
    }
    else {
      Serial.println("Sorry - could not set baud rate");
      Serial.println("Try powering off and on again");
      Serial.println("Don't try to use 115200");
    }
  }
}
