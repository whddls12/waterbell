package com.ssafy.fcc.controller;

import com.ssafy.fcc.MQTT.MqttSubscriber;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SystemController {

    // /system/facilities/{facility_id}/sensors/{category}/logs?period=

    private final SystemService systemService;

    @GetMapping("/facilities/{facility_id}/sensors/{category}/logs")
    public ResponseEntity<List<SensorLogDto>> systemLogList(@PathVariable("facility_id") int facilityId, @PathVariable String category) {

        List<SensorLogDto> logList;

        try {
            logList = systemService.getList(facilityId, category);
            if(logList.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logList, HttpStatus.OK);
    }


}
