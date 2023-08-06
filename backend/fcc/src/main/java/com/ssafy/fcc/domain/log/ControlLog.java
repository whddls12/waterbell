package com.ssafy.fcc.domain.log;


import com.ssafy.fcc.domain.facility.Facility;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class ControlLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "control_id")
    private Long controlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;


    @Enumerated(EnumType.STRING)
    private ControlType category;

    @Column(name = "control_time")
    private LocalDateTime controlTime;

    @Column(name = "water_height")
    private Integer waterHeight;

    @Enumerated(EnumType.STRING)
    private CommandType command;

}
