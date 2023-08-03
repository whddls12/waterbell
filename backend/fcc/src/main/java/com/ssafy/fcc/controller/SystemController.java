package com.ssafy.fcc.controller;

import com.ssafy.fcc.MQTT.MqttSubscriber;
import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SystemController {

    private final MqttSubscriber mqttSubscriber;

    @GetMapping("/")
    public String index() {
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard1").subscribe("Humidity");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard2").subscribe("A/7/Temp");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard3").subscribe("Dust");
        mqttSubscriber.init("tcp://172.20.10.12:1883","test").subscribe("picture");
        mqttSubscriber.init("tcp://172.20.10.12:1883", "DashBoard4").subscribe("A/11/height");
        return "index";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
    
}
