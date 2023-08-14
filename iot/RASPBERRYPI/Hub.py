import time,json,ssl
import paho.mqtt.client as mqtt
import threading

THING_NAME = ''
ENDPOINT = ""
CERTPATH =  "" # cert파일 경로
KEYPATH = "" # key 파일 경로
CAROOTPATH = "" # RootCaPem 파일 경로

FACILITY = "10"
QOS = 0
# PLATE or BOARD
ACTUATOR ="PLATE"

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT broker")
    else:
        print("Connection to MQTT broker failed")

# arduino -> iotcore
def connect_mqtt1():
    arduino = mqtt.Client()
    arduino.connect("localhost", 1883, 60)
    arduino.on_connect = on_connect
    return arduino

# iotcore -> arduino
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
    IoTCore.subscribe("Server/"+FACILITY)
    IoTCore.on_message = lambda client, userdata, msg: on_message2(IoTCore, userdata, msg, arduino)

def subscribe3(IoTCore, arduino):
    IoTCore.subscribe("Server/CAM1")
    IoTCore.on_message = lambda client, userdata, msg: on_message3(IoTCore, userdata, msg, arduino)
    
    IoTCore.subscribe("Server/CAM2")
    IoTCore.on_message = lambda client, userdata, msg: on_message4(IoTCore, userdata, msg, arduino)


def subscribe4(arduino, IoTCore):
    arduino.subscribe("Arduino/CAM1")
    arduino.on_message = lambda client, userdata, msg: on_message5(arduino, userdata, msg, IoTCore)
    arduino.subscribe("Arduino/CAM2")
    arduino.on_message = lambda client, userdata, msg: on_message6(arduino, userdata, msg, IoTCore)




def on_message1(client, userdata, msg, IoTCore):

    payload = msg.payload.decode()

    # print(f"Received message: {payload}")

    result = IoTCore.publish("Arduino/SENSOR", FACILITY+payload, QOS)
    status = result.rc

    if status == mqtt.MQTT_ERR_SUCCESS:
        print(f"Send `{payload}` to topic `Arduino/SENSOR`")
    else:
        print(f"Failed to send message to topic `Arduino/SENSOR`")


def on_message2(client, userdata, msg, arduino):

    payload = msg.payload.decode()

    # print(f"Received message : {payload}")
    # print(f"{userdata}")
    
    result = arduino.publish(ACTUATOR, payload, QOS)
    status = result.rc

    if status == mqtt.MQTT_ERR_SUCCESS:
        print(f"Send `{payload}` to topic `Server/PLATE`")


        
def on_message3(client, userdata, msg, arduino):

    payload = msg.payload.decode()

    result = arduino.publish("CAM1", payload, QOS)
    status = result.rc

    if status == mqtt.MQTT_ERR_SUCCESS:
        print(f"Send `{payload}` to topic `Server/PLATE`")

        
def on_message4(client, userdata, msg, arduino):

    payload = msg.payload.decode()

    result = arduino.publish("CAM2", payload, QOS)
    status = result.rc

    if status == mqtt.MQTT_ERR_SUCCESS:
        print(f"Send `{payload}` to topic `Server/PLATE`")


def on_message5(client, userdata, msg, arduino):

    result = IoTCore.publish("CAM1", msg.payload, QOS)
    status = result.rc


def on_message6(client, userdata, msg, arduino):

    result = IoTCore.publish("CAM2", msg.payload, QOS)
    status = result.rc

def run():
    arduino = connect_mqtt1()
    IoTCore = connect_mqtt2()
    subscribe1(arduino, IoTCore)
    subscribe2(IoTCore, arduino)
    subscribe3(IoTCore, arduino)
    subscribe4(arduino, iotcore)

    arduino_thread = threading.Thread(target=arduino.loop_forever)
    IoTCore_thread = threading.Thread(target=IoTCore.loop_forever)

    arduino_thread.start()
    IoTCore_thread.start()

    arduino_thread.join()
    IoTCore_thread.join()

if __name__ == '__main__':
    run()
