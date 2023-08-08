package com.ssafy.fcc.config;

import com.ssafy.fcc.MQTT.MqttSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTSubscriberConfig {

    private final MqttSubscriber mqttSubscriber;

    @Autowired
    public MQTTSubscriberConfig(MqttSubscriber mqttSubscriber) {
        this.mqttSubscriber = mqttSubscriber;
        initializeMQTTSubscribers();
    }

    private void initializeMQTTSubscribers() {
//        mqttSubscriber.init("tcp://192.168.0.10:1883", "DashBoard1").subscribe("Humidity");
//        mqttSubscriber.init("tcp://192.168.0.10:1883", "DashBoard2").subscribe("Temperature");
//        mqttSubscriber.init("tcp://192.168.0.10:1883", "DashBoard3").subscribe("Dust");
//        mqttSubscriber.init("tcp://192.168.0.10:1883", "DashBoard4").subscribe("height");
//        mqttSubscriber.init("tcp://192.168.0.10:1883", "test").subscribe("picture");
//        mqttSubscriber.init("tcp://192.168.43.41:1883","Cam").subscribe("Cam");
    }
}