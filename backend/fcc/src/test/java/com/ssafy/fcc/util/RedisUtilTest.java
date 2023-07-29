package com.ssafy.fcc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RedisUtilTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void 저장() throws InterruptedException {
        //given
        redisUtil.setDataExpire("test", "1234", 2000); // 2초 저장

        //when

        //then
        String test = redisUtil.getData("test");
        System.out.println(test);
        Assertions.assertEquals(test, "1234");

        Thread.sleep(2000);

        test = redisUtil.getData("test");
        System.out.println(test);
        Assertions.assertNull(test); //2초후 삭제
    }
}
