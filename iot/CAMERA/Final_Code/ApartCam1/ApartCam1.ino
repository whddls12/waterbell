#include "esp_camera.h"
#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h> //ArduinoJSON6
DynamicJsonDocument CONFIG(2048);

const char* ssid = ""; // wifi 이름
const char* password = ""; // wifi 비번
const char* mqtt_server = "";//mqtt주소
const char* HostName = "Apart1 CAM1"; // 이름
const char* topic_SUB = "Server/11/CAM"; // sub topic
const char* topic_PUB = "Arduino/11/CAM"; // pub topic

const char* mqttUser = "MQTT USER";
const char* mqttPassword = "MQTT PASSWORD";

WiFiClient espClient;
PubSubClient client(espClient);

long status = 1; 
// init : 0 (request x) 
// num (request)

const char* ON = "ON";
const char* OFF = "OFF";
unsigned long lastMsg = 0;



// sub시 실행
void callback(String topic, byte* message, unsigned int length) {
  String messageTemp;
  for (int i = 0; i < length; i++) {
    messageTemp += (char)message[i];
  }


  if (topic == topic_SUB) {
    // subscribe 

    if(messageTemp == ON){
      status++;
    }
    if(messageTemp == OFF){
      status--;
    }
  }


}

void camera_init() {
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer   = LEDC_TIMER_0;
  config.pin_d0       = 5;
  config.pin_d1       = 18;
  config.pin_d2       = 19;
  config.pin_d3       = 21;
  config.pin_d4       = 36;
  config.pin_d5       = 39;
  config.pin_d6       = 34;
  config.pin_d7       = 35;
  config.pin_xclk     = 0;
  config.pin_pclk     = 22;
  config.pin_vsync    = 25;
  config.pin_href     = 23;
  config.pin_sscb_sda = 26;
  config.pin_sscb_scl = 27;
  config.pin_pwdn     = 32;
  config.pin_reset    = -1;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;

  config.frame_size   = FRAMESIZE_VGA; // QVGA|CIF|VGA|SVGA|XGA|SXGA|UXGA
  config.jpeg_quality = 10;           
  config.fb_count     = 1;

  esp_err_t err = esp_camera_init(&config);

  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }
}


void take_picture() {
  camera_fb_t * fb = NULL;
  fb = esp_camera_fb_get();
  if (!fb) {
    Serial.println("Camera capture failed");
    return;
  }
  if (MQTT_MAX_PACKET_SIZE == 128) {
    //SLOW MODE (increase MQTT_MAX_PACKET_SIZE)
    client.publish_P(topic_UP, fb->buf, fb->len, false);
  }
  else {
    //FAST MODE (increase MQTT_MAX_PACKET_SIZE)
    client.publish(topic_UP, fb->buf, fb->len, false);
    Serial.println(fb->len);
  }

  Serial.println("CLIC");
  esp_camera_fb_return(fb);
}


void setup_wifi() {

  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.setHostname(HostName);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect(HostName, mqttUser, mqttPassword)) {
      
      Serial.println("connected");
      client.subscribe(topic_SUB);

    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}
void setup() {
  Serial.begin(115200);
  camera_init(); // init
  setup_wifi(); // wifi
  client.setServer(mqtt_server, 1883); // mqtt server
  client.setCallback(callback); 
}
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  unsigned long now = millis(); 
 
  // if(status > 0){
      lastMsg=now;
      take_picture();
  // }
}