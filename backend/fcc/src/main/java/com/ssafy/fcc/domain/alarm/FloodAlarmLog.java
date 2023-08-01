package com.ssafy.fcc.domain.alarm;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter @Setter
public class FloodAlarmLog extends Alarm {

    @Enumerated(EnumType.STRING)
    private Step step;


}
