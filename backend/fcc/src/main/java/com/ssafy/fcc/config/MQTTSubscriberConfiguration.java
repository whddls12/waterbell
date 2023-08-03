package com.ssafy.fcc.config;

import com.ssafy.fcc.MQTT.MqttSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTSubscriberConfiguration {

    private final MqttSubscriber mqttSubscriber;

    @Autowired
    public MQTTSubscriberConfiguration(MqttSubscriber mqttSubscriber) {
        this.mqttSubscriber = mqttSubscriber;
        initializeMQTTSubscribers();
    }

    private void initializeMQTTSubscribers() {
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard1").subscribe("Humid");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard2").subscribe("Temp");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard3").subscribe("Dust");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard4").subscribe("height");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "test").subscribe("picture");
    }
}