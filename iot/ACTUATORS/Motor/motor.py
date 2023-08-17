import time,json,ssl
import paho.mqtt.client as mqtt
import threading
import RPi.GPIO as GPIO


GPIO.setmode(GPIO.BCM)
StepPins = [8,9,10,11]
servo_pin = 18

GPIO.setup(servo_pin, GPIO.OUT)
GPIO.output(servo_pin, False)
for pin in StepPins:
    GPIO.setup(pin, GPIO.OUT)
    GPIO.output(pin, False)

StepCounter = 0
StepCount = 4

Seq = [[0, 0, 0, 1], [0, 0, 1, 0], [0, 1, 0, 0], [1, 0, 0, 0]]
RevSeq = [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]

# ON/OFF
sub_servo_topic = "Server/BOARD" 
sub_step_topic = "Server/PLATE" 

mqtt_server = "100.100.2.63"

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
    print(sub_servo_topic)
    arduino.subscribe(sub_servo_topic)
    arduino.message_callback_add(sub_servo_topic, on_message_servo)

    print(sub_step_topic)
    arduino.subscribe(sub_step_topic)
    arduino.message_callback_add(sub_step_topic, on_message_step)



# board
def on_message_servo(client, userdata, msg):
    payload = msg.payload.decode()
    pwm = GPIO.PWM(servo_pin, 50)
    pwm.start(3.0)
    timeA = 1.0


    if(payload == "ON"):
        print("ON")
        pwm.ChangeDutyCycle(7.5)

    else:
        print("OFF")
        pwm.ChangeDutyCycle(3.0)
    time.sleep(timeA)

# plate
def on_message_step(client, userdata, msg):
    payload = msg.payload.decode()
    print("ON")
    
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




def publisher(client, topic, message):
    client.publish(topic, message, QOS)


def run():
    arduino = connect_mqtt()

    subscribe(arduino)

    arduino_thread = threading.Thread(target=arduino.loop_forever)

    arduino_thread.start()

    arduino_thread.join()

if __name__ == '__main__':
    try:
        run()
    except KeyboardInterrupt:
        print("\nProgram interrupted. Performing cleanup...")
        # Add cleanup actions here if needed
        pwm.ChangeDutyCycle(0.0)
        pwm.stop()
        GPIO.cleanup()  # Cleanup GPIO pins if you're using them
        sys.exit(0)     # Exit the program gracefully
