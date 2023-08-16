
#include <PubSubClient.h>
#include <WiFiEsp.h>
#include <SoftwareSerial.h>
#include <AccelStepper.h>

#define motorPin1 6   // IN1
#define motorPin2 7   // IN2
#define motorPin3 8  // IN3
#define motorPin4 9  // IN4

unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE  (10)
char msg[MSG_BUFFER_SIZE];

SoftwareSerial espSerial(2, 3); // RX, TX
long int baudRate = 9600;

char ssid[] = "jjhjjh";             // your network SSID (name)
char pass[] = "123456700";        // your network password
int status = WL_IDLE_STATUS;      // the Wifi radio's status
char server[] = "172.20.10.5";    // IP address of the MQTT server
// Server/시설번호/plate나 보드 - ON OFF

char Sub_topic[] = "Server/PLATE";            // Default topic string
char Pub_topic[] = "Arduino/CONTROL";

char clientId[] = "Motor1";      // Cliwent id: Must be unique on the broker

WiFiEspClient wifi;               // Initialize the Ethernet client object
PubSubClient mqttClient(wifi);    // Initialize the MQTT client

AccelStepper stepper(AccelStepper::FULL4WIRE, motorPin1, motorPin3, motorPin2, motorPin4);

int motorStatus = 0; // 0 : OFF 1: ON
int operate = 0; // 0: default, 1 : up, 2 : down 

int interval = 5000; // send status time 

bool flag = true;
byte* str;

void setup() {
  Serial.begin(115200);

  // Set baud rate of ESP8266 to 9600 regardless of original setting
  set_esp8266_baud_rate(baudRate);

  stepper.setMaxSpeed(1000);     // 최대 속도 설정 (steps per second)
  stepper.setAcceleration(50);  // 가속도 설정 (steps per second squared)

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

  mqttClient.setServer(server, 1883);
  mqttClient.setCallback(onReceive);
}

void runMotorClock(){
  stepper.moveTo(512);
  stepper.run();
}

void runMotorRevClock(){
  stepper.moveTo(-512);
  stepper.run();
}

void loop() {

  if (!mqttClient.connected()) {
    reconnect();
  }
  mqttClient.loop();

  // unsigned long now = millis();
  // if(flag){
  //   if (operate == 1){
  //     // up
  //     runMotorClock();
  //     operate = 0;
  //   }else if(operate == 2){
  //     //down
  //     runMotorRevClock();
  //     operate = 0;
  //   }
  //   if(now - lastMsg > interval){
  //     lastMsg = now;
  //     snprintf (msg, MSG_BUFFER_SIZE, "%d", motorStatus);
  //     mqttClient.publish(Pub_topic, msg);
  //   }
  // }else{
  //   Serial.print("falsesss");
  //   setMotor();
  // }
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
  
  flag = false;
  str = payload;

  // for (int i=0;i<length;i++) {
  //   Serial.print((char)payload[i]);
  // }


}

void setMotor(){
  // if(str == "1"){
  //   motorStatus = 1;
  //   operate = 1;
  // }else if(str =="OFF"){
  //   motorStatus = 0;
  //   operate = 2;
  // }
  Serial.print(flag);
 if (strcmp((char*)str, "1") == 0) {
    motorStatus = 1;
    operate = 1;
  } else if (strcmp((char*)str, "OFF") == 0) {
    motorStatus = 0;
    operate = 2;
  }
  flag = true;
}

void reconnect() {
  // Loop until we're reconnected
  while (!mqttClient.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (mqttClient.connect(clientId)) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      mqttClient.publish(Pub_topic,"hello world");
      // ... and resubscribe
      mqttClient.subscribe(Sub_topic);
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
