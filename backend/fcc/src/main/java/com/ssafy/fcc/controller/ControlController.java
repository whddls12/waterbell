package com.ssafy.fcc.controller;

import com.ssafy.fcc.MQTT.MqttPublisher;
import com.ssafy.fcc.service.MqttPubSubService;
import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
public class ControlController {

    private final SystemService systemService;
    private final MqttPubSubService mqttPubSubService;

    @PostMapping("/manager/{facility_id}/{command}")
    public ResponseEntity<Integer> command(@PathVariable("facility_id") int facilityId, @PathVariable String command) {


        try {
            String topic = "";
            switch (command) {
                case "ON":
                    System.out.println("!!!!!!!!!!!!!!!");
                    // 차수판 동작 명령, cotrolLog 저장
                    topic = String.format("%d/activation", facilityId);
                    mqttPubSubService.publishMessage(topic, "activate");
                    systemService.insertControlLog(facilityId, "ON");

                    // 동작 알림


                    break;
                case "OFF":
                    // 차수판 해제 명령, cotrolLog 저장
                    topic = String.format("%d/deactivation", facilityId);
                    mqttPubSubService.publishMessage(topic, "deactivate");
                    systemService.insertControlLog(facilityId, "OFF");

                    // 해제 알림

                    break;
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(1);

    }
}
