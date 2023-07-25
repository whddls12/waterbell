package com.ssafy.fcc.domain.facility;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class UndergroundRoad extends Facility {

    private String undergroundRoadName;

    @Column(columnDefinition = "decimal(18,10)")
    private BigDecimal latitude;

    @Column(columnDefinition = "decimal(18,10)")
    private BigDecimal longitude;


}
