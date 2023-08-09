package com.ssafy.fcc.dto;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

public class MqttMessage extends AWSIotMessage {
    public MqttMessage(String topic, String payload, AWSIotQos qos) {
        super(topic, qos, payload);
    }

    @Override
    public void onSuccess() {
        System.out.println("Message Published Sucessfully");
    }

    @Override
    public void onFailure() {
        System.out.println("Message Published failed");
    }

    @Override
    public void onTimeout() {
        System.out.println("Message Published Timeout");
    }
}
