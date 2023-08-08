package com.ssafy.fcc.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearch {

    private String name;
    private Integer addressNumber;
    private Integer page;
    private String apartCode;
}
