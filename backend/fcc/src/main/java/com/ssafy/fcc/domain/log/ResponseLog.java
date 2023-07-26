package com.ssafy.fcc.domain.log;

import com.ssafy.fcc.domain.facility.Facility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ResponseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger responseLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private String category;

    private LocalDateTime responseTime;

    private boolean status;


}
