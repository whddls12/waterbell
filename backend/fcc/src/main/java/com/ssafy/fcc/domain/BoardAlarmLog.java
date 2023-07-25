package com.ssafy.fcc.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class BoardAlarmLog extends Alarm{

    private int apartBoardId;
    private int undergroundBoardId;
}
