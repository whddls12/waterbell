package com.ssafy.fcc.domain.alarm;

import com.ssafy.fcc.domain.alarm.Alarm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("B")
@Getter @Setter
public class BoardAlarmLog extends Alarm {

    private Integer apartBoardId;
    private Integer undergroundBoardId;
}
