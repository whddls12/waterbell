
char Sensor = A0; //A0에 센서연결
int Sensor_val; //센서 ADC값 저장 변수
int lcd_pos;
byte i;
#define VOLTS_PER_UNIT .0049F // (.0049 for 10 bit A-D) 
float volts;
float inches;
float cm;
void setup() // 초기화
{
Serial.begin(9600);//시리얼통신 속도설정 
}
void loop() // 무한루프
{
  Sensor_val = analogRead(Sensor); // 센서저장변수에 아날로그값을 저장
  volts = (float)Sensor_val * VOLTS_PER_UNIT; // 아날로그값을 volt 단위로 변환
  inches = 23.897 * pow(volts,-1.1907); // 측정전압에 따른 inch단위 거리 계산
  cm = 60.495 * pow(volts,-1.1904); // 측정전압에 따른 cm단위 거리 계산
//   if (volts < .2) inches = -1.0; // 측정범위 미만일때
// if(cm>=99){
//   Serial.print(cm);

// }
// else{
//   Serial.print(cm);
// }
  Serial.print(cm);
  Serial.println(" ");
  delay(1000); //2초간 지연
}