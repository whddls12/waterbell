package com.ssafy.fcc.dto;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class MqttTopic extends AWSIotTopic {
    public MqttTopic(String topic){
        super(String.valueOf(topic));
    }

    @Override
    public void onMessage(AWSIotMessage message) {

        System.out.println(message.getTopic());
        System.out.println(message.getQos());
        System.out.println(message.getStringPayload());
    }
}
