package com.ssafy.fcc.service;

import com.ssafy.fcc.util.RedisUtil;
import com.ssafy.fcc.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsCertificationService {

    private final SmsUtil smsUtil;
    private final RedisUtil redisUtil;

    @Value("${systemPhoneNumber}")
    private String systemPhoneNumber;
    //인증만료시간(3분)
    private static final int EXPIRATION_TIME = 180000;

    // 인증번호를 전송하고 redis에 저장
    public void sendSms(String phoneNumber) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        //4자리 난수 생성
        Random rand = new Random();
        int number = rand.nextInt(1000000);
        String strNumber = String.format("%06d", number);

        //redis에 저장
        redisUtil.setDataExpire(phoneNumber, strNumber, EXPIRATION_TIME);
        //sms 인증번호 전송
        smsUtil.sendSMS(systemPhoneNumber, phoneNumber, smsUtil.makeSmsContent(strNumber));
    }

    // 사용자가 입력한 인증번호가 redis와 일치한지 확인
    public int verifySms(String phoneNumber, String AuthenticationNumber){
        String value = redisUtil.getData(phoneNumber);
        // 만료기한이 지난 경우 or 번호가 잘못된 경우
        if(value==null) return 0;
        // 인증번호 일치
        if(value.equals(AuthenticationNumber)){
            redisUtil.deleteData(phoneNumber);
            return 2;
        }
        // 인증번호 불일치
        else{
            return 1;
        }
    }
}
