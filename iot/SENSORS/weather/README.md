# weather

### HW List
- Arduino Uno
- ESP 8266 (WiFi Module)
- DHT11 (Temperature and Humidity Sensor)
- Resistor (1k$\Omega$)


### Pin Map

| 센서 | 센서 pin | Arduino 핀| Sensor Pin |
|:--:|:--:|:--:|:--:|
|ESP8266|TX|||
|ESP8266|RX|||
|ESP8266|EN|3.3V||
|ESP8266|Data1|Digital 2||
|ESP8266|Data2|Digital 3||
|VCC_ESP8266||3.3V|
|VCC_DHT11||5V|
|GND||GND|




### Circuit






### How to set library
1. DHT 11
- 목적 : 온습도 센서

2. Softwareserial
- 목적 : Degital Pin을 TX/RX로 사용


3. PubSubClient
- 목적 : MQTT 통신

4. WiFiEsp
- 목적 : SoftwareSerial를 사용하여 Internet과의 통신
- 다운로드 : https://github.com/bportaluri/WiFiEsp
  

### Sensor Spec
1. DHT 11
   
| 센서 | 값 |
|:--:|:--:|
|습도 측정 범위 | 20-90% RH|
|습도 오차 범위 | ±5% RH|
|측정 온도 범위 | 0-50 °C|
|온도 오차 범위 | ±2% °C|
|동작 전압 | 5V|
|소비 전력 | 저전력|

2. 
