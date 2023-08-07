package com.ssafy.fcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MypageApartManagerDto {
    private Integer id;
    private String loginId;
    private String phone;
    private String ApartCode;
    private  String apartName;
    private String address;

}
