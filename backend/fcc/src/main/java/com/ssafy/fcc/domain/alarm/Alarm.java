package com.ssafy.fcc.domain.alarm;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private LocalDateTime regDate;

    @Column(length = 50)
    private String content;

    private Boolean isApart;

    private Boolean isFlood;

    @OneToMany(mappedBy = "alarm")
    private List<ReceiveAlarmMember> receiveAlarmMembers = new ArrayList<>();
}
