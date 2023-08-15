import time,json,ssl
import paho.mqtt.client as mqtt
import threading
import RPi_I2C_driver

mylcd = RPi_I2C_driver.lcd()


def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT broker")
    else:
        print("Connection to MQTT broker failed")

def connect_mqtt():
    arduino = mqtt.Client()
    arduino.connect("localhost", 1883, 60)
    arduino.on_connect = on_connect
    return arduino

def subscribe(arduino):
    arduino.subscribe("Arduino/SENSOR")
    arduino.on_message = lambda client, userdata, msg: on_message1(arduino, userdata, msg, IoTCore)


def on_message(client, userdata, msg):
    global msg_count
    payload = msg.payload.decode()
    print(f"Received message: {payload}")


def run():
    arduino = connect_mqtt1()

    subscribe(arduino, IoTCore)


    arduino_thread = threading.Thread(target=arduino.loop_forever)


    arduino_thread.start()


    arduino_thread.join()


if __name__ == '__main__':
    run()

