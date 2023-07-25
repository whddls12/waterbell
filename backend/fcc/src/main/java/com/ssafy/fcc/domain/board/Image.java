package com.ssafy.fcc.domain.board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_board_id", nullable = true)
    private ApartBoard apartBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underground_road_board_id", nullable = true)
    private UndergroundRoadBoard undergroundRoadBoard;

    private String imageName;

    private String imagePath;

}
