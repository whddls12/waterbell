package com.ssafy.fcc.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.member.ApartMember;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class ApartBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    @JsonIgnore
    private Apart apart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private ApartMember apartMember;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    private LocalDateTime createDate;

    private Integer viewCount;



}
