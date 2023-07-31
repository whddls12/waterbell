package com.ssafy.fcc.domain.alarm;

import com.ssafy.fcc.domain.alarm.Alarm;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ReceiveAlarmMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recive_alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isRead;

    private LocalDateTime readDate;
}
