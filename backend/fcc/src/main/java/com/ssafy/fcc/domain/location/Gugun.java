package com.ssafy.fcc.domain.location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Gugun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gugun_id")
    private Integer id;

    private String gugunName;

    @ManyToOne
    @JoinColumn(name = "sido_id")
    private Sido sido;

}
