package com.ssafy.fcc.domain.alarm;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class FloodAlarmLog extends Alarm {

    private int step;
}
