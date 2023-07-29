package com.ssafy.fcc.controller;

import com.ssafy.fcc.service.SmsCertificationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api("핸드폰 본인 인증")
public class VerificationController {

    private final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    private final SmsCertificationService smsCertificationService;

    @PostMapping("/verification/code")
    public ResponseEntity<Map<String, String>> send_code(@RequestParam String phoneNumber) {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            smsCertificationService.sendSms(phoneNumber);
            resultMap.put("message", "전송완료");
            status = HttpStatus.OK;
        } catch (Exception e) {
            logger.error("SMS 전송 실패 : {}", e);
            resultMap.put("message", "전송 실패");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/verification/code")
    public ResponseEntity<Map<String, String>> verify_code(@RequestParam String phoneNumber, @RequestParam String code){
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            int verified = smsCertificationService.verifySms(phoneNumber, code);
            if (verified == 1) {
                resultMap.put("message", "인증완료");
                status = HttpStatus.ACCEPTED;
            } else if(verified == 2){
                resultMap.put("message", "인증번호가 틀렸습니다.");
                status = HttpStatus.BAD_REQUEST;
            }
            else {
                resultMap.put("message", "인증번호 만료기간이 지났습니다.");
                status = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception e) {
            logger.error("인증 실패 : {}", e);
            resultMap.put("message", "인증 실패");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}
