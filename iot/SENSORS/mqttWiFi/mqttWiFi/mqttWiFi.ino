
#include <PubSubClient.h>
#include <WiFiEsp.h>
#include <SoftwareSerial.h>

SoftwareSerial espSerial(2, 3); // RX, TX
long int baudRate = 9600;

char ssid[] = "jjhjjh";             // your network SSID (name)
char pass[] = "123456700";        // your network password
int status = WL_IDLE_STATUS;      // the Wifi radio's status
char server[] = "192.168.43.41";    // IP address of the MQTT server
char topic[] = "test";            // Default topic string
char clientId[] = "Arduino weather";      // Cliwent id: Must be unique on the broker

WiFiEspClient wifi;               // Initialize the Ethernet client object
PubSubClient mqttClient(wifi);    // Initialize the MQTT client

void setup() {
  Serial.begin(115200);

  // Set baud rate of ESP8266 to 9600 regardless of original setting
  set_esp8266_baud_rate(baudRate);

  
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

void loop() {
  char message[256]="connected!";
  int i;


  if (Serial.available()) {
    Serial.print("publish!!");
    Serial.readBytesUntil("\r",message, 255);
    mqttClient.publish(topic, message);
  }

  // mqttClient.publish(topic, "sussess!!!");

  if (!mqttClient.connected()) {
    reconnect();
  }

  mqttClient.loop();

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
