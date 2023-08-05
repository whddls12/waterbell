package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.facility.Facility;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorLogDto {

    private long id;
    private LocalDateTime sensorTime;
    private String facilityName;
    private String category;
    private Integer value;
}
