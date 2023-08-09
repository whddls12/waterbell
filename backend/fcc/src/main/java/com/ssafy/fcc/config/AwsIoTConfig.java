package com.ssafy.fcc.config;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.ssafy.fcc.dto.MqttMessage;
import com.ssafy.fcc.dto.MqttTopic;
import com.ssafy.fcc.dto.raspPayload;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AwsIoTConfig {

    String clientEndpoint = "a3kqjba8huf6r2-ats.iot.us-east-2.amazonaws.com";
    String clientId = "rasp";
    String awsAccessKeyId = "AKIARGSGEM3WGE2RSOK4";
    String awsSecretAccessKey = "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";

    AWSIotMqttClient client = null;

    public void connectToIoT() throws AWSIotException {
        client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();
        System.out.println("Connected to IoT");
    }

    public void publish(raspPayload payload) throws AWSIotException {
        String topic = "test";
        AWSIotQos qos = AWSIotQos.QOS0;
        long timeout = 3000;

        String testPayload ="successsss!!!!";

//        MqttMessage message = new MqttMessage(topic, qos, payload.toString());
        MqttMessage message = new MqttMessage(topic, qos, testPayload);
        client.publish(message, timeout);
    }

    public void subscribe(String topicName) throws AWSIotException {
        MqttTopic topic = new MqttTopic(topicName);

        client.subscribe(topic);
    }
}
