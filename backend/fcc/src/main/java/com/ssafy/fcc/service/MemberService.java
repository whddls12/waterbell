package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public  Member login(String loginId, String password) {
        Member loginMember = memberRepository.findLogin(loginId, password);

        if(loginMember !=null)
            return loginMember;
        else return null;
    }

    public Member findByLoginId(String loginId){
        Member loginMember = memberRepository.findByLoginId(loginId);

        if(loginMember !=null )
            return loginMember;
        return null;
    }

    //시스템 회원 가입
    @Transactional
    public Integer joinApartMember(Member member) throws Exception{
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
    public boolean validationDuplicateId(String loginId){
        Member member = memberRepository.findByLoginId(loginId);
        if(member !=null) return false;
        return true; //중복 없음 통과
    }
}
