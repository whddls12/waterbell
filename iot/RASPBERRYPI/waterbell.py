import time,json,ssl
import paho.mqtt.client as mqtt

#AWS IoT 관련 설정
THING_NAME = 'IoT'
CERTPATH =  "" # cert파일 경로
KEYPATH = "" # key 파일 경로
CAROOTPATH = "" # RootCaPem 파일 경로

ENDPOINT = "" # 엔드포인트

ArduinoList = ["TEMP", "DUST", "HUMID", "HEIGHT", "CAM"]
IoTCoreList = ["PLATE", "BOARD"]

FacilityId = "1"
QOS = 0

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT broker")
    else:
        print("Connection to MQTT broker failed")

#Arduino
def connect_mqtt1():
    arduino = mqtt.Client()
    arduino.connect("localhost", 1883, 60)
    arduino.on_connect = on_connect

    return arduino

#IoTCore
def connect_mqtt2():
    IoTCore = mqtt.Client()
    IoTCore.on_connect = on_connect
    IoTCore.tls_set(CAROOTPATH, certfile= CERTPATH, keyfile=KEYPATH, tls_version=ssl.PROTOCOL_TLSv1_2, ciphers=None)
    IoTCore.connect(ENDPOINT, 8883, 60)

    return IoTCore


# 각 메시지가 들어왔을 때 실행할 코드
def on_message_temp(client, userdata, msg):
    # TEMP 
    global IoTCore
    topic = "Arduino/TEMP"
    payload = FacilityId + msg.payload.decode()
    publisher(Arduino, topic, payload):
    pass

def on_message_dust(client, userdata, msg):
    # DUST 
    global IoTCore
    topic = "Arduino/DUST"
    payload = FacilityId + msg.payload.decode()
    publisher(Arduino, topic, payload):

    pass

def on_message_humid(client, userdata, msg):
    # HUMID 
    global IoTCore
    topic = "Arduino/HUMID"
    payload = FacilityId + msg.payload.decode()
    publisher(Arduino, topic, payload):
    pass

def on_message_height(client, userdata, msg):
    # HEIGHT 
    global IoTCore
    topic = "Arduino/HEIGHT"
    payload = FacilityId + msg.payload.decode()
    publisher(Arduino, topic, payload):
    pass

def on_message_cam(client, userdata, msg):
    # CAM 
    global IoTCore
    topic = "Arduino/CAM"
    payload = FacilityId + msg.payload.decode()
    publisher(Arduino, topic, payload):
    pass

def on_message_plate(client, userdata, msg):
    # PLATE
    global arduino
    topic = "Server/PLATE"
    payload = FacilityId + msg.payload.decode()
    publisher(arduino, topic, payload):

    pass

def on_message_board(client, userdata, msg):
    # BOARD
    global arduino
    topic = "Server/PLATE"
    payload = FacilityId + msg.payload.decode()
    publisher(arduino, topic, payload):

    pass


def subscribe(arduino, IoTCore):

    for sensor in ArduinoList:
        topic = "Arduino/"+name

        arduino.subscribe(topic)
        if sensor == "TEMP":
            arduino.message_callback_add(topic, on_message_temp)
        elif sensor == "DUST":
            arduino.message_callback_add(topic, on_message_dust)
        elif sensor == "HUMID":
            arduino.message_callback_add(topic, on_message_humid)
        elif sensor == "HEIGHT":
            arduino.message_callback_add(topic, on_message_height)
        elif sensor == "CAM":
            arduino.message_callback_add(topic, on_message_cam)

    for Actuator in IoTCoreList:
        topic = "Server/"+FacilityId+"/"+name

        IoTCore.subscribe(topic)
        if sensor == "PLATE":
            IoTCore.message_callback_add(topic, on_message_plate)
        elif sensor == "BOARD":
            IoTCore.message_callback_add(topic, on_message_board)

def publisher(client, topic, message):
    client.publish(topic, message, QOS)




def run():
    global arduino
    global IoTCore 
    arduino = connect_mqtt1()
    IoTCore = connect_mqtt2()
    subscribe(arduino, IoTCore)
    
    try:
        arduino.loop_forever()


    except KeyboardInterrupt:
        print("Exiting...")
        arduino.disconnect()
        IoTCore.disconnect()

if __name__ == '__main__':
    run()