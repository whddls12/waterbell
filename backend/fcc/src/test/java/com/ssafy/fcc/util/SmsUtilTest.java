package com.ssafy.fcc.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class SmsUtilTest {
    @Autowired SmsUtil smsUtil;

    @Test(expected = RuntimeException.class)
    public void 문자전송실패() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        //given
        String from = "1";
        String to = "01073877808";
        String content = "문자 테스트";

        //when
        smsUtil.sendSMS(from, to, content);

        //then
        //예외발생
    }
}