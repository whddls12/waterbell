package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.member.ApartManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartManagerResponse extends MemberResponse{

    private int facilityId;

    public ApartManagerResponse(ApartManager apartManager,TokenDto token){
        super(apartManager,token);
        facilityId = apartManager.getId();
    }
}
