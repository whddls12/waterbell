package com.ssafy.fcc.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinMemberDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String apartCode;
    private Integer addressNumber;

}
