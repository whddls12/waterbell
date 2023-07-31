package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.domain.member.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Integer id;
    private String loginId;
    private String role;
    private String phone;
    private String accessToken;
    private String refreshToken;


    public MemberResponse(Member member, TokenDto token) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.role = member.getRole().toString();
        this.phone = member.getPhone();
        this.accessToken = token.getAccessToken();
        this.refreshToken=token.getRefreshToken();
    }
}
