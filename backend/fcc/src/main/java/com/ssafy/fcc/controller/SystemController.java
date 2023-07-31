package com.ssafy.fcc.controller;

import com.ssafy.fcc.MQTT.MqttPublisher;
import com.ssafy.fcc.MQTT.MqttSubscriber;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemController {

    @GetMapping("/")
    public String index() {
        MqttSubscriber heightSensor = new MqttSubscriber();
        heightSensor.init("tcp://172.20.10.8:1883", "DashBoard1").subscribe("heightSensor");

        // 위에서 값 받아오면 DB에 저장

        return "index";
    }
    
}
