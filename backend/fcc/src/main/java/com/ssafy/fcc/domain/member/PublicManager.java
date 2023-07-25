package com.ssafy.fcc.domain.member;

import com.ssafy.fcc.domain.location.Sido;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter @Getter
public class PublicManager extends Member{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private Sido sido;

}
