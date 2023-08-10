# iot

## How to set Raspberry pi
### 1. Install Mosquitto 
1. mosquitto 인증키 다운로드

```
wget http://repo.mosquitto.org/debian/mosquitto-repo.gpg.key
sudo apt-key add mosquitto-repo.gpg.key
```


1. mosquitto 저장소 패키지 등록

```
cd /etc/apt/source.list.d/
sudo wget http://repo.mosquitto.org/debian/mosquitto-stretch.list
```

2. MQTT 브로커 설치

```
sudo apt-get update
sudo apt-cache search mosquitto
sudo apt-get install mosquitto mosquitto-clients
```

— 설치 완료 —

3. Set up mosquitto.conf
   
```
cd /etc/mosquitto/
sudo nano mosquitto.conf
```

```
# Place your local configuration in /etc/mosquitto/conf.d/
#
# A full description of the configuration file is at
# /usr/share/doc/mosquitto/examples/mosquitto.conf.example

pid_file /run/mosquitto/mosquitto.pid

persistence true
persistence_location /var/lib/mosquitto/

log_dest file /var/log/mosquitto/mosquitto.log

include_dir /etc/mosquitto/conf.d
```
아래에 두줄 추가

```
bind_address 0.0.0.0
allow_anonymous true
```
0.0.0.0 -> 모든 ip 접속 허용

4. restart mosquitto
   
```
sudo systemctl restart mosquitto
```

1. mosquitto 실행

```
sudo /etc/init.d/mosquitto start
```

— Sub, Pub 테스트 —

4. subscribe 등록

```
mosquitto_sub -d -t hello/world
```

5. publish 입력

```
mosquitto_pub -d -t hello/world -m "Hi!"
```


## How to set Arduino
### 1. Install Arduino IDE
https://www.arduino.cc/en/software/


## How to set Window
### 1. Install Mosquitto 

1. Install mosquitto
   
https://mosquitto.org/download/

2. Set up mosquitto.conf

C:\Program Files\mosquitto.conf 경로의 파일에서 아래 두줄 추가

```
listener 1883 0.0.0.0
allow_anonymous true
```

listener -> 허용할 ip주소, port    
allow_anonymous -> client id 유무에 다른 허용 여부

3. Start Mosquitto
```
cd "C:\Program Files\mosquitto"
mosquitto -v -c mosquitto.conf
```

4. subscribe 등록

```
mosquitto_sub -d -t hello/world
```

5. publish 입력

```
mosquitto_pub -d -t hello/world -m "Hi!"
```




## How to set Spring for MQTT
### 1. MQTT Dependences (gradle)
```java
//mqtt
	implementation 'org.springframework.boot:spring-boot-starter-integration'
	implementation 'org.springframework.integration:spring-integration-mqtt'
	implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.1'
```
Publisher와 Subscriber를 eclipsepaho를 사용하여 구현   
https://eclipse.dev/paho/index.php?page=clients/java/index.php