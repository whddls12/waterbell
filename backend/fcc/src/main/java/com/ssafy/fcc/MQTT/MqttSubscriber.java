package com.ssafy.fcc.MQTT;

import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MqttSubscriber implements MqttCallback {

    public MqttClient mqttclient;

    public final SystemService systemService;


    //Mqtt프로토콜를 이용해서 broker에 연결하면서 연결정보를 설정할 수 있는 객체
    public MqttConnectOptions mqttOption;
    public MqttSubscriber init(String server, String clientId) {
        try {
            mqttOption = new MqttConnectOptions();
            mqttOption.setCleanSession(true);
            mqttOption.setKeepAliveInterval(30);

            mqttclient = new MqttClient(server, clientId);

            mqttclient.setCallback(this);
            mqttclient.connect(mqttOption);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        String[] result = split(message.toString());

        try {
            String category = result[0];
            int value = Integer.parseInt(result[result.length - 1]);
            System.out.println(category + " " + value);
            saveLog(category,value);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLog(String category, int value) {
        systemService.insertLog(category,value);
    }


    public String[] split(String message) {
        return message.split(" ");
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    // 구독 신청
    public boolean subscribe(String topic) {

        boolean result = true;
        if (topic != null) {
            try {
                mqttclient.subscribe(topic, 0);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

}
