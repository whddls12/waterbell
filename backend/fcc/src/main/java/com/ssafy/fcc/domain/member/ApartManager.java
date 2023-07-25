package com.ssafy.fcc.domain.member;

import com.ssafy.fcc.domain.facility.Apart;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ApartManager extends Member {

    @OneToOne(fetch = FetchType.LAZY)
    private Apart apart;

}
