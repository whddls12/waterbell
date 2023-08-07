package com.ssafy.fcc.controller;

import com.ssafy.fcc.MQTT.MqttSubscriber;
import com.ssafy.fcc.domain.log.CommandType;
import com.ssafy.fcc.dto.ControlLogDto;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SystemController {

    // /system/facilities/{facility_id}/sensors/{category}/logs?period=

    private final SystemService systemService;

    @GetMapping("/facilities/{facility_id}/sensors/{category}/logs/{page}")
    public ResponseEntity<Map<String, Object>> SensorLogList(@PathVariable("facility_id") int facilityId,
                                                             @PathVariable String category,
                                                             @PathVariable int page,
                                                             @RequestParam(value = "searchStartDate", required = false, defaultValue = "1900-01-01T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchStartDate,
                                                             @RequestParam(value = "searchEndDate", required = false, defaultValue = "9999-12-31T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchEndDate) {

        Map<String, Object> logList;

        try {
            logList = systemService.getSensorLogList(facilityId, category, page, searchStartDate, searchEndDate);
            if (logList.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logList, HttpStatus.OK);
    }

    @GetMapping("/facilities/{facility_id}/control/logs/{page}")
    public ResponseEntity<Map<String, Object>> ControlLogList(@PathVariable("facility_id") int facilityId,
                                                              @PathVariable int page,
                                                              @RequestParam(value = "searchStartDate", required = false, defaultValue = "1900-01-01T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchStartDate,
                                                              @RequestParam(value = "searchEndDate", required = false, defaultValue = "9999-12-31T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchEndDate) {

        Map<String, Object> logList;

        try {
            logList = systemService.getControlLogList(facilityId, page, searchStartDate, searchEndDate);
            if (logList.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logList, HttpStatus.OK);
    }

    @PostMapping("/facilities/{facility_id}/control/logs/{command}")
    public int saveControlLog(@PathVariable("facility_id") Integer facilityId, @PathVariable String command) {

        int result = systemService.insertControlLog(facilityId, command);

        return result;
    }


}
