package com.ssafy.fcc.domain.log;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum SensorType {

    @Enumerated(EnumType.STRING)
    HEIGHT,
    @Enumerated(EnumType.STRING)
    TEMP,
    @Enumerated(EnumType.STRING)
    HUMID,
    @Enumerated(EnumType.STRING)
    DUST,
    @Enumerated(EnumType.STRING)
    CAM,
}
