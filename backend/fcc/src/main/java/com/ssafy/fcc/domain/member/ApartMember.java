package com.ssafy.fcc.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.fcc.domain.facility.Apart;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ApartMember extends Member{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    @JsonIgnore
    private Apart apart;

   private boolean isSystem; //로그인 타입, 자체 로그인 true, 소셜로그인 false

    @Enumerated(EnumType.STRING)
    private PlatformType platformType;

    private String name;

    private int addressNumber;

}
