package com.ssafy.fcc.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.ssafy.fcc.config.AwsIoTConfig;
import com.ssafy.fcc.dto.raspPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttPubSubService {

    @Autowired
    AwsIoTConfig mqttConfig;

    public void publishMessage(raspPayload payload) throws AWSIotException {
        mqttConfig.connectToIoT();
        mqttConfig.publish(payload);
    }

    public void subscribeTopic(String topic) throws AWSIotException {
        mqttConfig.connectToIoT();
        mqttConfig.subscribe(topic);
    }

}
