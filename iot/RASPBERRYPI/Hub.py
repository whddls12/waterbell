import time,json,ssl
import paho.mqtt.client as mqtt
import threading

THING_NAME = 'IoT'
ENDPOINT = "a221zxhtj4qlos-ats.iot.us-east-2.amazonaws.com"
CERTPATH =  "/home/jjhjjh/Desktop/Hub/IoT.cert.pem" # cert파일 경로
KEYPATH = "/home/jjhjjh/Desktop/Hub/IoT.private.key" # key 파일 경로
CAROOTPATH = "/home/jjhjjh/Desktop/Hub/root-CA.crt" # RootCaPem 파일 경로
FACILITY = "10/"
msg_count = 0

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT broker")
    else:
        print("Connection to MQTT broker failed")

def connect_mqtt1():
    arduino = mqtt.Client()
    arduino.connect("localhost", 1883, 60)
    arduino.on_connect = on_connect
    return arduino

def connect_mqtt2():
    IoTCore = mqtt.Client()
    IoTCore.tls_set(CAROOTPATH, certfile= CERTPATH, keyfile=KEYPATH, tls_version=ssl.PROTOCOL_TLSv1_2, ciphers=None)
    IoTCore.connect(ENDPOINT, 8883, 60)
    IoTCore.on_connect = on_connect
    return IoTCore

def subscribe1(arduino, IoTCore):
    arduino.subscribe("Arduino/SENSOR")
    arduino.on_message = lambda client, userdata, msg: on_message1(arduino, userdata, msg, IoTCore)

def subscribe2(IoTCore, arduino):
    IoTCore.subscribe("Server/10/BOARD")
    IoTCore.on_message = lambda client, userdata, msg: on_message2(IoTCore, userdata, msg, arduino)

def on_message1(client, userdata, msg, IoTCore):
    global msg_count
    payload = msg.payload.decode()
    print(f"Received message: {payload}")

    result = IoTCore.publish("Arduino/SENSOR", FACILITY+payload, qos=1)
    status = result.rc
    if status == mqtt.MQTT_ERR_SUCCESS:
        print(f"Send `{payload}` to topic `Arduino/SENSOR`")
    else:
        print(f"Failed to send message to topic `Arduino/SENSOR`")


def on_message2(client, userdata, msg, arduino):
    global msg_count
    payload = msg.payload.decode()
    print(f"Received message : {payload}")
    print(f"{userdata}")
    result = arduino.publish("Server/PLATE",payload,qos=0)
    status = result.rc
    if status == mqtt.MQTT_ERR_SUCCESS:
        print(f"Send `{payload}` to topic `Server/PLATE`")

def run():
    arduino = connect_mqtt1()
    IoTCore = connect_mqtt2()
    subscribe1(arduino, IoTCore)
    subscribe2(IoTCore,arduino)


    arduino_thread = threading.Thread(target=arduino.loop_forever)
    IoTCore_thread = threading.Thread(target=IoTCore.loop_forever)

    arduino_thread.start()
    IoTCore_thread.start()

    arduino_thread.join()
    IoTCore_thread.join()

if __name__ == '__main__':
    run()
