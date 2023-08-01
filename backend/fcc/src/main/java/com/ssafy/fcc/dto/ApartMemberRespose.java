package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartMemberRespose extends MemberResponse{

    private int facilityId;
    private String name;
    private int addressNumber;
    public ApartMemberRespose(ApartMember member, TokenDto token){
        super(member, token);
        facilityId = member.getId();
        name= member.getName();
        addressNumber = member.getAddressNumber();
    }
}
