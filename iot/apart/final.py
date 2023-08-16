import time,json,ssl
import paho.mqtt.client as mqtt
import threading
import RPi.GPIO as GPIO
import RPi_I2C_driver


mylcd = RPi_I2C_driver.lcd()

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

GPIO.setup(16,GPIO.OUT)
GPIO.setup(20,GPIO.OUT)
GPIO.setup(21,GPIO.OUT)


StepPins = [8,9,10,11]
for pin in StepPins:
    GPIO.setup(pin, GPIO.OUT)
    GPIO.output(pin, False)

StepCounter = 0
StepCount = 4

Seq = [[0, 0, 0, 1], [0, 0, 1, 0], [0, 1, 0, 0], [1, 0, 0, 0]]
RevSeq = [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]

# ON/OFF
sub_step_topic = "Server/PLATE" 
sub_lcd_topic = "Server/STATUS"
pub_topic = "Arduino/CONTROL"

mqtt_server = "172.30.1.38"

facility_id = "11"

# 0 : OFF 1: ON
stepStatus = 0;

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT broker")
    else:
        print("Connection to MQTT broker failed")

def connect_mqtt():
    arduino = mqtt.Client()
    arduino.connect(mqtt_server, 1883, 60)
    arduino.on_connect = on_connect
    return arduino

def subscribe(arduino):
    arduino.subscribe(sub_step_topic)
    arduino.message_callback_add(sub_step_topic, on_message_step)

    arduino.subscribe(sub_lcd_topic)
    arduino.message_callback_add(sub_lcd_topic, on_message_lcd)


def on_message_lcd(client, userdata, msg):
    payload = msg.payload.decode()
    print(f"Received message: {payload}")

    if(payload == "DEFAULT"):
        mylcd.lcd_clear()
        mylcd.lcd_display_string("WaterBell", 1)
        mylcd.lcd_display_string("Have a nice day!", 2)
        GPIO.output(16,True)
        GPIO.output(20,False)
        GPIO.output(21,False)
    elif(payload == "WORKING"):
        mylcd.lcd_clear()
        mylcd.lcd_display_string("Danger!!",1)
        mylcd.lcd_display_string("STOP! No entry",2)
        GPIO.output(16,False)
        GPIO.output(21,True)
        GPIO.output(20,False)
    else:
        mylcd.lcd_clear()
        mylcd.lcd_display_string("Warning!!",1)
        mylcd.lcd_display_string("Move carfully",2)
        GPIO.output(16,False)
        GPIO.output(21,False)
        GPIO.output(20,True)


# plate
def on_message_step(client, userdata, msg):
    global stepStatus
    payload = msg.payload.decode()

    if(payload == "ON"):
        stepStatus = 1
        cnt = 0
        StepCounter = 0
        while cnt<512:
            for pin in range(0,4):
                xpin = StepPins[pin]
                if Seq[StepCounter][pin]!=0:
                    GPIO.output(xpin, True)
                else:
                    GPIO.output(xpin, False)
            StepCounter += 1
            if (StepCounter == StepCount):
                StepCounter = 0
            if (StepCounter<0):
                StepCounter = StepCount
            cnt+=1
            time.sleep(0.01)

    else :
        print("OFF")
        stepStatus = 0
        StepCounter = 0
        cnt = 0
        while cnt < 512:
            for pin in range(0, 4):
                xpin = StepPins[pin]
                if RevSeq[StepCounter][pin]!=0:
                    GPIO.output(xpin, True)

                else:
                    GPIO.output(xpin, False)
            StepCounter +=1
            if(StepCounter == StepCount) :
                StepCounter = 0
            if(StepCounter <0):
                StepCounter = STepCount
            cnt+=1

            time.sleep(0.01)


def publish_status(client, topic, interval):
    global stepStatus
    while True:
        client.publish(topic, stepStatus, qos = 0)
        time.sleep(interval)



def publisher(client, topic, message):
    QOS = 0
    client.publish(topic, message, QOS)


def run():
    global stepStatus
    arduino = connect_mqtt()

    subscribe(arduino)

    arduino_thread = threading.Thread(target=arduino.loop_forever)
    pub_thread = threading.Thread(target = publish_status, args=(arduino,pub_topic, 5))

    arduino_thread.start()
    pub_thread.start()

    arduino_thread.join()
    pub_thread.join()


if __name__ == '__main__':
    try:
        run()
    except KeyboardInterrupt:
        print("\nProgram interrupted. Performing cleanup...")
        # Add cleanup actions here if needed
        GPIO.cleanup()  # Cleanup GPIO pins if you're using them
        mylcd.lcd_clear()
        mylcd.backlight(0)
        sys.exit(0)     # Exit the program gracefully
