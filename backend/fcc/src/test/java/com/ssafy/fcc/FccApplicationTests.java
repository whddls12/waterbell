package com.ssafy.fcc;

import com.ssafy.fcc.service.SmsCertificationService;
import com.ssafy.fcc.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
class FccApplicationTests {

	@Autowired
	SmsCertificationService smsCertificationService;

	@Autowired
	RedisUtil redisUtil;

	@Test
	void 저장(){
		redisUtil.setDataExpire("asd", "qwe", 100000);
	}

	@Test
	void contextLoads() {
		try {
			smsCertificationService.sendSms("01073877808");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

}
