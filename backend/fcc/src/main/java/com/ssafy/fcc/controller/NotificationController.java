package com.ssafy.fcc.controller;

import com.ssafy.fcc.handler.MyWebSocketHandler;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api("알림")
public class NotificationController {

    private final MyWebSocketHandler myWebSocketHandler;

    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(@RequestParam String message, @RequestParam String userId) throws IOException {
        myWebSocketHandler.sendNotificationToSpecificUser(userId, message);
        return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
    }
}

