package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.domain.member.PublicManager;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicManagerResponse extends MemberResponse {

    private int sidoId;
    private List<Integer> facilityId;

    public PublicManagerResponse(PublicManager publicManager,TokenDto token){
        super(publicManager, token);
        sidoId = publicManager.getId();
        facilityId = new ArrayList<>();
    }


}
