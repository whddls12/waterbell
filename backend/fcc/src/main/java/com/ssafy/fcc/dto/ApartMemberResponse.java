package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartMemberResponse extends MemberResponse{

    private int facilityId;
    private String name;
    private int addressNumber;
    private String platformToken;
    private PlatformType platformType;

    public ApartMemberResponse(ApartMember member, TokenDto token){
        super(member, token);
        facilityId = member.getId();
        name= member.getName();
        addressNumber = member.getAddressNumber();
    }
}
