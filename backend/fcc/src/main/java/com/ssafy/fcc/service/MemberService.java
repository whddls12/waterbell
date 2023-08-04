package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.member.*;
import com.ssafy.fcc.dto.ApartManagerResponse;
import com.ssafy.fcc.dto.ApartMemberResponse;
import com.ssafy.fcc.dto.PublicManagerResponse;
import com.ssafy.fcc.dto.TokenDto;
import com.ssafy.fcc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final JwtTokenProvider jwtTokenProvider;

    public Member login(String loginId, String password) {
        Member loginMember = memberRepository.findLogin(loginId, password);

        if (loginMember != null)
            return loginMember;
        else return null;
    }

    public Member findByLoginId(String loginId) throws Exception {
        Member loginMember = memberRepository.findByLoginId(loginId);


        if (loginMember == null) {
            throw new Exception("일치하는 회원이 없습니다.");
        }
        return loginMember;

    }

    //시스템 회원 가입
    @Transactional
    public Integer joinApartMember(Member member) throws Exception {
        if (memberRepository.findByLoginId(member.getLoginId()) != null) {
            throw new Exception("아이디 중복입니다.");
        }
        System.out.println("중복 아이디 없음");
        if (memberRepository.findByPhone(member.getPhone()) != null) {
            throw new Exception("이미 동일한 휴대폰 번호로 가입한 이력이 있습니다.");
        }
        System.out.println("중복 휴대폰 없음");

        memberRepository.save(member);
        System.out.println("저장 완료");

        return member.getId();
    }

    //아이디 중복 검증
    public boolean validationDuplicateId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member != null) return false;
        return true; //중복 없음 통과
    }

    //id로 멤버 찾기
    public Member findById(int id) {
        return memberRepository.findById(id);
    }



    public Object getMemberResponse(Member member, String token) {

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(token);
        if (member.getRole() == Role.APART_MEMBER) {
            ApartMemberResponse apartMemberResponse = new ApartMemberResponse((ApartMember) member, tokenDto);
            return apartMemberResponse;
        } else if (member.getRole() == Role.APART_MANAGER) {
            ApartManagerResponse apartManagerResponse = new ApartManagerResponse((ApartManager) member, tokenDto);
            return apartManagerResponse;
        } else if (member.getRole() == Role.PUBLIC_MANAGER) {
            PublicManagerResponse publicManagerResponse = new PublicManagerResponse((PublicManager) member, tokenDto);
            return publicManagerResponse;
        }
        return null;
    }

    @Transactional
    public void modifyPassword(Integer id, String newPw) {
        Member m = memberRepository.findById(id);
        m.setPassword(newPw);
    }


    public ApartMember getMemberByNameAndPhone(String name, String phone) {
        return memberRepository.findByPhoneAndName(name, phone);
    }
}
