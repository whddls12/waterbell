package com.ssafy.fcc.domain.facility;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Apart extends  Facility{

    private  String apartName;

    private String apartCode;
}

