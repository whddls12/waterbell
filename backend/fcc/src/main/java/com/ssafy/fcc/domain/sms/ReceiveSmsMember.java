package com.ssafy.fcc.domain.sms;

import com.ssafy.fcc.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ReceiveSmsMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recive_sms_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sms_id")
    private SmsLog smslog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
