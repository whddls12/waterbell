package com.ssafy.fcc.controller;

import com.ssafy.fcc.MQTT.MqttSubscriber;
import com.ssafy.fcc.domain.log.CommandType;
import com.ssafy.fcc.dto.ControlLogDto;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SystemController {

    // /system/facilities/{facility_id}/sensors/{category}/logs?period=

    private final SystemService systemService;

    @GetMapping("/facilities/{facility_id}/sensors/{category}/logs")
    public ResponseEntity<List<SensorLogDto>> SensorLogList(@PathVariable("facility_id") int facilityId, @PathVariable String category) {

        List<SensorLogDto> logList;

        try {
            logList = systemService.getSensorLogList(facilityId, category);
            if(logList.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logList, HttpStatus.OK);
    }

    @GetMapping("/facilities/{facility_id}/control/logs")
    public ResponseEntity<List<ControlLogDto>> ControlLogList(@PathVariable("facility_id") int facilityId) {

        List<ControlLogDto> logList;

        try {
            logList = systemService.getControlLogList(facilityId);
            if(logList.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logList, HttpStatus.OK);
    }

    @PostMapping("/facilities/{facility_id}/control/logs/{command}")
    public int saveControlLog(@PathVariable("facility_id") Integer facilityId, @PathVariable String command) {

        int result = systemService.insertControlLog(facilityId,command);

        return result;
    }




}
