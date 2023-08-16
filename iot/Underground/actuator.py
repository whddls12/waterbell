import time,json,ssl
import paho.mqtt.client as mqtt
import threading
import RPi_I2C_driver
import RPi.GPIO as GPIO

mylcd = RPi_I2C_driver.lcd()

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

GPIO.setup(16,GPIO.OUT)
GPIO.setup(20,GPIO.OUT)
GPIO.setup(21,GPIO.OUT)

servo_pin = 18
GPIO.setup(servo_pin, GPIO.OUT)
GPIO.output(servo_pin, False)

# 0 : OFF 1: ON
servoStatus = 0

sub_servo_topic = "Server/BOARD"
sub_lcd_topic = "Server/STATUS"

facility_id = "10"

pub_topic = "Arduino/CONTROL"


def publish_status(client, topic, interval):
    global servoStatus
    while True:
        client.publish(topic, servoStatus, qos = 0)
        time.sleep(interval)

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT broker")
    else:
        print("Connection to MQTT broker failed")

def connect_mqtt():
    arduino = mqtt.Client()
    arduino.connect("172.20.10.8", 1883, 60)

    arduino.on_connect = on_connect
    return arduino

def subscribe(arduino):
    arduino.subscribe(sub_servo_topic)
    arduino.message_callback_add(sub_servo_topic, on_message_servo)


    arduino.subscribe(sub_lcd_topic)
    arduino.message_callback_add(sub_lcd_topic, on_message_lcd)

def on_message_servo(client, userdata, msg):
    global servoStatus
    pwm = GPIO.PWM(servo_pin, 50)
    payload = msg.payload.decode()
    
    pwm.start(3.0)
    timeA = 1.0

    if(payload == "ON"):
        print("ON")
        pwm.ChangeDutyCycle(7.5)

    else:
        print("OFF")
        pwm.ChangeDutyCycle(3.0)
    time.sleep(timeA)



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

def publisher(client, topic, message):
    QOS = 0
    client.publish(topic, message, QOS)



def run():
    arduino = connect_mqtt()

    subscribe(arduino)

    arduino_thread = threading.Thread(target=arduino.loop_forever)
    pub_thread = threading.Thread(target = publish_status, args=(arduino, pub_topic, 5))


    arduino_thread.start()

    arduino_thread.join()

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
