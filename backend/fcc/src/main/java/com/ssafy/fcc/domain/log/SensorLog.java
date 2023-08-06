package com.ssafy.fcc.domain.log;

import com.ssafy.fcc.domain.facility.Facility;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class SensorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Enumerated(EnumType.STRING)
    private SensorType category;

    @Column(name = "sensor_time")
    private LocalDateTime sensorTime;

    @Column(name = "sensor_data")
    private Integer sensorData;

}
