package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.dto.BoardAlarmDto;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.service.AlarmService;
import com.ssafy.fcc.service.ApartManagerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final MyWebSocketHandler myWebSocketHandler;
    private final ApartManagerService apartManagerService;
    private final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    //차수판 가동시 알림
    @PostMapping("/notification/apartManager/activation")
    public ResponseEntity<Map<String, Object>> sendActivationNotification() {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            // 관리자 건물의 입주민들 웹 알림
            apartManagerService.sendFloodWebNotification(member_id, Step.ACTIVATION);
            // 문자 알림
            apartManagerService.sendSmsWebNotification(member_id, Step.ACTIVATION);
            resultMap.put("message", "전송완료");
            status = HttpStatus.OK;
        } catch (Exception e){
            logger.error("알림 전송 실패 : {}", e);
            resultMap.put("message", "전송실패");
            resultMap.put("excetpion", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //차수판 해제시 알림
    @PostMapping("/notification/apartManager/deactivation")
    public ResponseEntity<Map<String, Object>> sendDeactivationNotification() throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            // 관리자 건물의 입주민들 웹 알림
            apartManagerService.sendFloodWebNotification(member_id, Step.DEACTIVATION);
            // 문자 알림
            apartManagerService.sendSmsWebNotification(member_id, Step.DEACTIVATION);
            resultMap.put("message", "전송완료");
            status = HttpStatus.OK;
        } catch (Exception e){
            logger.error("알림 전송 실패 : {}", e);
            resultMap.put("message", "전송 실패");
            resultMap.put("excetpion", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}

