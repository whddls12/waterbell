package com.ssafy.fcc.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class SmsLog {

    @Id @GeneratedValue
    @Column(name = "sms_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime sendDate;

    @Column(length = 100)
    private String content;

    @OneToMany(mappedBy = "smslog")
    private List<ReceiveMember> receiveMember = new ArrayList<>();
}
