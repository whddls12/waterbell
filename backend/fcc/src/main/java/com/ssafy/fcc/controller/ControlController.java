package com.ssafy.fcc.controller;

//import com.ssafy.fcc.MQTT.MqttPublisher;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.ControlType;
import com.ssafy.fcc.service.FacilityService;
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
    private final FacilityService facilityService;

    @PostMapping("/{facility_id}/{command}")
    public ResponseEntity<Integer> command(@PathVariable("facility_id") int facilityId, @PathVariable String command) {

        Facility facility = facilityService.findById(facilityId);
        String sensorType = facility.isApart() == true ? "PLATE" : "BOARD";
        try {
            String topic = "";
            switch (command) {
                case "ON":
                    // 차수판 동작 명령, cotrolLog 저장, 시설 상태 WORKING
                    topic = String.format("Server/%d/%s", facilityId, sensorType);
                    mqttPubSubService.publishMessage(topic, "ON");
                    systemService.insertControlLog(facilityId, "ON");
                    facilityService.updateStatus(facility, WaterStatus.WORKING);
                    topic = String.format("Server/%d/STATUS",facilityId);
                    mqttPubSubService.publishMessage(topic,"WORKING");
                    // 동작 알림


                    break;
                case "OFF":
                    // 차수판 해제 명령, cotrolLog 저장, 시설 상태 SECOND, 사이렌/LED 동작
                    topic = String.format("Server/%d/%s", facilityId, sensorType);
                    mqttPubSubService.publishMessage(topic, "OFF");
                    systemService.insertControlLog(facilityId, "OFF");
                    facilityService.updateStatus(facility, WaterStatus.SECOND);
                    topic = String.format("Server/%d/STATUS",facilityId);
                    mqttPubSubService.publishMessage(topic,"SECOND");

                    // 해제 알림

                    break;
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }

        return ResponseEntity.status(HttpStatus.OK).body(1);

    }
}
