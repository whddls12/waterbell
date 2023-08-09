package com.ssafy.fcc.controller;

import com.amazonaws.services.iot.client.AWSIotException;
import com.ssafy.fcc.dto.raspPayload;
import com.ssafy.fcc.service.MqttPubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MQTTController {


    @Autowired
    MqttPubSubService service;

    @PostMapping("/publish")
    public String publishMessaged(@RequestBody raspPayload payload) throws AWSIotException {
        service.publishMessage(payload);

        return "message Published Successfully";
    }

    @PostMapping("/subscribe")
    public String subscribeTopic(@RequestParam String topic) throws AWSIotException {
        service.subscribeTopic(topic);

        return "topic subscribed Successfully";
    }


}
