
package com.ssafy.fcc.MQTT;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;



@Component
public class MqttPublisher {

    //MQTT 통신에서 구독자와 발행자의 역할을 하기 위한 기능을 가진 객체
    private MqttClient client;

//    public MqttPublisher() {
//        try {
//            client = new MqttClient("tcp:192.168.0.10:1883","java");
//            client.connect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean send(String topic, String msg) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes()); // 실제 broker로 전송할 메세지
            client.publish(topic,message);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void close() {
        if(client != null) {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        MqttPublisher sender = new MqttPublisher();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                String msg = "";
                while (true) {
                    if (i == 10) break;
                    else {
                        if (i % 2 == 1) msg = "led_on";
                        else msg = "led_off";
                    }
                    sender.send("exercise", msg);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sender.close();
            }
        }).start();
    }
}