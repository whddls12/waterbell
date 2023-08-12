package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.domain.member.ApartManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartDto {

    private Integer id;

    private Integer gugun_id;

    private boolean isApart;

    private String firstFloodMessage;

    private String activation_message;

    private String deactivation_message;

    private Integer firstAlarmValue;

    private Integer secondAlarmValue;

    private String hubIp;

    private WaterStatus status;

    private  String apartName;

    private String apartCode;

    private String address;

    private Integer apartManager_id;

    public ApartDto(Apart apart){
        this.id = apart.getId();
        this.gugun_id = apart.getGugun().getId();
        this.isApart = apart.isApart();
        this.firstFloodMessage = apart.getFirstFloodMessage();
        this.activation_message = apart.getActivation_message();
        this.deactivation_message = apart.getDeactivation_message();
        this.firstAlarmValue = apart.getFirstAlarmValue();
        this.secondAlarmValue = apart.getSecondAlarmValue();
        this.hubIp = apart.getHubIp();
        this.status = apart.getStatus();
        this.apartName = apart.getApartName();
        this.apartCode = apart.getApartCode();
        this.address = apart.getAddress();
        this.apartManager_id = apart.getApartManager().getId();
    }
}
