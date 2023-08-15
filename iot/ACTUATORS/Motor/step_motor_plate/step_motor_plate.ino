#include <AccelStepper.h>
#include <PubSubClient.h>
#include <WiFiEsp.h>
#include <SoftwareSerial.h>

#define motorPin1 6   // IN1
#define motorPin2 7   // IN2
#define motorPin3 8  // IN3
#define motorPin4 9  // IN4


unsigned long lastMsg = 0;

AccelStepper stepper(AccelStepper::FULL4WIRE, motorPin1, motorPin3, motorPin2, motorPin4);

void setup() {
   Serial.begin(115200);
  stepper.setMaxSpeed(1000);     // 최대 속도 설정 (steps per second)
  stepper.setAcceleration(50);  // 가속도 설정 (steps per second squared)
}

  int flag = 0;
void loop() {
  // 원하는 위치로 모터 이동
  // stepper.moveTo(512);  // 한바퀴 : 2048 스텝만큼 이동
  // stepper.run();         // 모터 동작
  unsigned long now = millis();
    if(flag == 1){
      stepper.moveTo(512);
      Serial.print("1");
      stepper.run(); 
    }else if(flag == 0){
      stepper.moveTo(-512);
      // Serial.print("0");
      stepper.run(); 
    }
    if(now - lastMsg == 5000){
      lastMsg=now;
      flag += 1;
      flag %=2;

    }


}
