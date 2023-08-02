package com.ssafy.fcc.domain.board;

import com.ssafy.fcc.domain.facility.UndergroundRoad;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class UndergroundRoadBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private UndergroundRoad undergroundRoad;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String name;
    
    private String phone;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    private LocalDateTime createDate;

    private Integer viewCount;

    private int boardPassword; //4자리 숫자
}
