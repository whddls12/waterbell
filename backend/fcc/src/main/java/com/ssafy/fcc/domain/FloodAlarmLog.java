package com.ssafy.fcc.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("F")
@Getter @Setter
public class FloodAlarmLog extends Alarm{

    private int step;
}
