package com.ssafy.fcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
@AllArgsConstructor
@Getter
@ToString
//@RedisHash(value = "jwtToken", timeToLive = 60*60*24*14) //레디스 스토리지에 저장
public class RefreshToken {

    @Id
    private Integer id;

    private String refreshToken;


}