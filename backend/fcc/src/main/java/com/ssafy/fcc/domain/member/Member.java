package com.ssafy.fcc.domain.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //json 결과로 출력하지 않기 위함
    private String password;

    private String refreshToken;

    private LocalDateTime createdAt;

    private boolean state; //활성, 비활성

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime expiredAt;

    private String phone;

}