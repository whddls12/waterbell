package com.ssafy.fcc.controller;

import com.ssafy.fcc.service.SmsCertificationService;
import com.ssafy.fcc.util.RedisUtil;
import com.ssafy.fcc.util.SmsUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@Api("핸드폰 본인 인증")
public class VerificationController {

    private final SmsCertificationService smsCertificationService;

    @PostMapping("/verification/code")
    public ResponseEntity send_code(@RequestParam String phoneNumber) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        smsCertificationService.sendSms(phoneNumber);
        return new ResponseEntity<>("전송완료", HttpStatus.OK);
    }

    @GetMapping("/verification/code")
    public ResponseEntity verify_code(@RequestParam String phoneNumber, @RequestParam String code){
        if (smsCertificationService.verifySms(phoneNumber, code)) {
            return new ResponseEntity<>("인증완료", HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>("인증실패", HttpStatus.BAD_REQUEST);
        }
    }
}
