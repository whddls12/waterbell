# waterHeight

### HW List
- Arduino Uno
- ESP 8266 (WiFi Module)
- IRDistanceSensor


### Pin Map

| 센서 | 센서 pin | Arduino 핀|
|:--:|:--:|:--:|
|ESP8266|TX||
|ESP8266|RX||
|ESP8266|EN|3.3V|
|ESP8266|Data1|Digital 2|
|ESP8266|Data2|Digital 3|
|IRDistanceSen|Data|Analog 0|
|VCC_IRDistance|VCC|5V|
|GND|GND|GND|




### Circuit






### How to set library
1. Softwareserial
- 목적 : Degital Pin을 TX/RX로 사용

2. PubSubClient
- 목적 : MQTT 통신

3. WiFiEsp
- 목적 : SoftwareSerial를 사용하여 Internet과의 통신
- 다운로드 : https://github.com/bportaluri/WiFiEsp
  

### Sensor Spec
1. IR Distance Sensor
   
| 센서 | 값 |
|:--:|:--:|

2. ESP8266 (WiFi module)


