import time,json,ssl
import paho.mqtt.client as mqtt
import threading

#AWS IoT 관련 설정
THING_NAME = 'IoT'
ENDPOINT = "a221zxhtj4qlos-ats.iot.us-east-2.amazonaws.com"
CERTPATH =  "/home/jjhjjh/Desktop/IoT.cert.pem" # cert파일 경로
KEYPATH = "/home/jjhjjh/Desktop/IoT.private.key" # key 파일 경로
CAROOTPATH = "/home/jjhjjh/Desktop/root-CA.crt" # RootCaPem 파일 경로


ArduinoList = ["SENSOR", "CAM1", "CAM2", "CONTROL"]
IoTCoreList = ["PLATE", "BOARD", "CAM1", "CAM2","STATUS"]

FacilityId = "10"
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
def on_message_sensor(client, userdata, msg):
    # SENSOR 
    global IoTCore
    topic = "Arduino/SENSOR"
    print("send")
    payload = FacilityId + "/" + msg.payload.decode()
    publisher(IoTCore, topic, payload)
    pass

def on_message_cam1(client, userdata, msg):
    # CAM 
    global IoTCore
    topic = "Arduino/"+FacilityId+"/CAM1"
    publisher(IoTCore, topic, msg.payload)
    pass

def on_message_cam2(client, userdata, msg):
    # CAM 
    global IoTCore
    topic = "Arduino/"+FacilityId+"/CAM2"
    publisher(IoTCore, topic, msg.payload)
    pass

def on_message_control(client, userdata, msg):
    # control state
    global IoTCore
    topic = "Arduino/CONTROL"
    payload = FacilityId + "/"+msg.payload.decode()
    publisher(IoTCore, topic, payload)
    pass


def on_message_plate(client, userdata, msg):
    # PLATE
    global arduino
    topic = "Server/PLATE"

    payload = msg.payload.decode()
    publisher(arduino, topic, payload)

    pass

def on_message_board(client, userdata, msg):
    # BOARD
    global arduino
    topic = "Server/BOARD"
    payload = msg.payload.decode()
    publisher(arduino, topic, payload)

    pass



def on_message_cam3(client, userdata, msg):
    # BOARD
    global arduino
    topic = "CAM1"
    # payload = FacilityId + msg.payload.decode()
    publisher(arduino, topic, msg)

    pass

    
def on_message_cam4(client, userdata, msg):
    # BOARD
    global arduino
    topic = "CAM2"
    # payload = FacilityId + msg.payload.decode()
    publisher(arduino, topic, msg)

    pass

def on_message_status(client, userdata, msg):
    print("receive")
    topic = "Server/STATUS"
    payload = msg.payload.decode()
    publisher(arduino, topic, payload)

def subscribe(arduino, IoTCore):

    # Arduino -> Server
    for sensor in ArduinoList:
        topic = "Arduino/"+sensor
        arduino.subscribe(topic)

        if sensor == "SENSOR":
            arduino.message_callback_add(topic, on_message_sensor)
        elif sensor == "CAM1":
            arduino.message_callback_add(topic, on_message_cam1)
        elif sensor == "CAM2":
            arduino.message_callback_add(topic, on_message_cam2)
        elif sensor == "CONTROL":
            arduino.message_callback_add(topic, on_message_control)

    # Server -> Arduino
    for Actuator in IoTCoreList:
        topic = "Server/"+FacilityId+"/"+Actuator

        IoTCore.subscribe(topic)
        if Actuator == "PLATE":
            IoTCore.message_callback_add(topic, on_message_plate)
        elif Actuator == "BOARD":
            IoTCore.message_callback_add(topic, on_message_board)
        elif Actuator == "CAM1":
            IoTCore.message_callback_add(topic, on_message_cam3)
        elif Actuator == "CAM2":
            IoTCore.message_callback_add(topic, on_message_cam4)
        elif Actuator == "STATUS":
            IoTCore.message_callback_add(topic, on_message_status)

def publisher(client, topic, message):
    client.publish(topic, message, QOS)
    print("yes")



def run():
    global arduino
    global IoTCore
    arduino = connect_mqtt1()
    IoTCore = connect_mqtt2()
    subscribe(arduino, IoTCore)
    
    arduino_thread = threading.Thread(target=arduino.loop_forever)
    IoTCore_thread = threading.Thread(target=IoTCore.loop_forever)

    arduino_thread.start()
    IoTCore_thread.start()

    arduino_thread.join()
    IoTCore_thread.join()

if __name__ == '__main__':
    try:
        run()
    except KeyboardInterrupt:
        print("\nProgram interrupted. Performing cleanup...")
        # Add cleanup actions here if needed
        sys.exit(0)     # Exit the program gracefully
