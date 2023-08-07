package com.ssafy.fcc.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyPageUserDto {
    private Integer id;
    private String loginId;
    private String phone;
}
