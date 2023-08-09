package com.ssafy.fcc.config;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.ssafy.fcc.dto.MqttMessage;
import com.ssafy.fcc.dto.MqttTopic;
import com.ssafy.fcc.dto.raspPayload;
import com.ssafy.fcc.service.SystemService;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AwsIoTConfig {

    String clientEndpoint = "a221zxhtj4qlos-ats.iot.us-east-2.amazonaws.com";
    String clientId = "IoTCore";
    String awsAccessKeyId = "AKIASBP5HSYQYFK435XS";
    String awsSecretAccessKey = "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";

    AWSIotMqttClient client = null;


    private final SystemService systemService;

    public AwsIoTConfig(SystemService systemService) throws AWSIotException {
        this.systemService = systemService;
        this.client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();
        System.out.println("Connected to IoT");

        subscribeToTopics();
    }

    private void subscribeToTopics() throws AWSIotException {
        String[] topics = {"TEMPERATURE", "Dust", "HUMIDITY","CAM"};

        for (String topicName : topics) {
            subscribeToTopic(topicName);
        }
    }

    private void subscribeToTopic(String topicName) throws AWSIotException {
        client.subscribe(new MqttTopic(topicName) {
            @Override
            public void onMessage(AWSIotMessage message) {
                try {
                    handleReceivedMessage(topicName, message.getStringPayload());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void handleReceivedMessage(String topic, String message) throws Exception {
        systemService.fromSensor(topic,message);
    }


    public void publish(String topic, String message) throws AWSIotException {
        AWSIotQos qos = AWSIotQos.QOS0;
        long timeout = 3000;

        MqttMessage mqttMessage = new MqttMessage(topic, message, qos);
        client.publish(mqttMessage, timeout);
    }

    public void subscribe(String topicName) throws AWSIotException {
        MqttTopic topic = new MqttTopic(topicName);
        client.subscribe(topic);
    }
}
