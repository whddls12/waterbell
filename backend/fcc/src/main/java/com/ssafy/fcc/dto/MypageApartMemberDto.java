package com.ssafy.fcc.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MypageApartMemberDto {

    private Integer id;
    private String loginId;
    private String phone;
    private String ApartCode;
    private  String apartName;
    private String address;
    private Integer addressNumber;
    private String name;
}
