package com.ssafy.fcc.dto;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyPagePubilicManagerDto {
    private Integer id;
    private String loginId;
    private String phone;

    private String sidoName;
}
