package com.ssafy.fcc.domain.facility;

import com.ssafy.fcc.domain.member.ApartManager;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Apart extends Facility{

    private  String apartName;

    private String apartCode;

    private String address;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "apart")
    private ApartManager apartManager;
}

