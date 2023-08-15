package com.ssafy.fcc.controller;


import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SystemController {

    private final SystemService systemService;

    @GetMapping("/manager/facilities/{facility_id}/sensors/{category}/logs/{page}")
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

    @GetMapping("/manager/facilities/{facility_id}/control/logs/{page}")
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

    @PostMapping("/manager/facilities/{facility_id}/control/logs/{command}")
    public int saveControlLog(@PathVariable("facility_id") int facilityId, @PathVariable String command) {

        int result = systemService.insertControlLog(facilityId, command);

        return result;
    }

    @GetMapping("/manager/facilities/{facility_id}/response/logs/{page}")
    public ResponseEntity<Map<String, Object>> responseLogList(@PathVariable("facility_id") int facilityId,
                                                               @PathVariable int page,
                                                               @RequestParam(value = "searchStartDate", required = false, defaultValue = "1900-01-01T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchStartDate,
                                                               @RequestParam(value = "searchEndDate", required = false, defaultValue = "9999-12-31T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchEndDate) {
        Map<String, Object> logList;

        try {
            logList = systemService.getResponseLogList(facilityId, page, searchStartDate, searchEndDate);
            if (logList.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(logList, HttpStatus.OK);
    }

    @GetMapping("/manager/facilities/{facilityId}/sensors/HEIGHT/latest")
    public int getLatestHeightSensor(@PathVariable int facilityId) {

        return systemService.getLatestHeightSensor(facilityId);
    }

    @GetMapping("/manager/{facility_id}/alarm/web/logs/{category}/send/{page}")
    public ResponseEntity<Map<String, Object>> alarmWebSendLogList(@PathVariable("facility_id") int facilityId,
                                                                   @PathVariable String category, @PathVariable int page,
                                                                   @RequestParam(value = "searchStartDate", required = false, defaultValue = "1900-01-01T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchStartDate,
                                                                   @RequestParam(value = "searchEndDate", required = false, defaultValue = "9999-12-31T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchEndDate) {
        Map<String, Object> logList = new HashMap<>();
        HttpStatus httpStatus;
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            logList = systemService.getAlarmSendWebLogList(facilityId, member_id, category, page, searchStartDate, searchEndDate);
            if (logList == null || logList.size() == 0) {
                httpStatus = HttpStatus.NO_CONTENT;
                logList.put("message", "데이터가 없습니다.");
            } else {
                logList.put("message", "조회완료.");
                httpStatus = HttpStatus.OK;
            }
        } catch (Exception e) {
            logList.put("message", "서버 에러.");
            logList.put("error", e);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(logList, httpStatus);
    }

    @GetMapping("/manager/{facility_id}/alarm/sms/logs/flood/send/{page}")
    public ResponseEntity<Map<String, Object>> alarmSmsSendLogList(@PathVariable("facility_id") int facilityId,
                                                                   @PathVariable int page,
                                                                   @RequestParam(value = "searchStartDate", required = false, defaultValue = "1900-01-01T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchStartDate,
                                                                   @RequestParam(value = "searchEndDate", required = false, defaultValue = "9999-12-31T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime searchEndDate) {
        Map<String, Object> logList = new HashMap<>();
        HttpStatus httpStatus;
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            logList = systemService.getAlarmSendSmsLogList(facilityId, member_id, page, searchStartDate, searchEndDate);
            if (logList == null || logList.size() == 0) {
                httpStatus = HttpStatus.NO_CONTENT;
                logList.put("message", "데이터가 없습니다.");
            } else {
                logList.put("message", "조회완료.");
                httpStatus = HttpStatus.OK;
            }
        } catch (Exception e) {
            logList.put("message", "서버 에러.");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(logList, httpStatus);
    }
}
