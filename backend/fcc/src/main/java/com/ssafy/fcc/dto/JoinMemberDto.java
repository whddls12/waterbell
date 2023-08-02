package com.ssafy.fcc.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinMemberDto {

    private String name;
    private String loginId;
    private String password;
    private String phone;
    private String apartCode;
    private Integer addressNumber;

}
