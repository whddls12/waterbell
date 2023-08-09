package com.ssafy.fcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class raspPayload {

    private String topic;
    private Integer facilityId;
    private Integer content;


}
