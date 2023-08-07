package com.ssafy.fcc.domain.log;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum SensorType {

    @Enumerated(EnumType.STRING)
    HEIGHT,

    @Enumerated(EnumType.STRING)
    TEMPERATURE,
    @Enumerated(EnumType.STRING)
    HUMIDITY,
    @Enumerated(EnumType.STRING)
    DUST,
    @Enumerated(EnumType.STRING)
    CAM,
}
