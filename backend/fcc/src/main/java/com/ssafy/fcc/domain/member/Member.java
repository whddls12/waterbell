package com.ssafy.fcc.domain.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean state; //활성, 비활성

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private String phone;

}