package com.ssafy.fcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityManagementDto {

    private Integer id;
    private String firstFloodMessage;
    private String activation_message;
    private String deactivation_message;
    private Integer firstAlarmValue;
    private Integer secondAlarmValue;

}
