package com.ssafy.fcc.controller;


import com.ssafy.fcc.config.security.JwtTokenProvider;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.domain.member.Role;
import com.ssafy.fcc.dto.JoinMemberDto;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final FacilityService facilityService;
    private final JwtTokenProvider jwtTokenProvider; // jwt 토큰 생성
    private final PasswordEncoder passwordEncoder;

    //아이디 중복 조회
    @GetMapping("/join/apartMember/duplication/{loginId}")
    public ResponseEntity<String> checkDuplicationLoginId(@PathVariable String loginId) {

        if (memberService.validationDuplicateId(loginId))
            return new ResponseEntity<String>("success", HttpStatus.OK);
        else
            return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
    }


    //아파트 코드 - 주소
    @PostMapping("/join/apartMember/validationApartCode")
    public ResponseEntity<String> checkValidationApartCode(@RequestBody Map<String, String> request) {

        String apartCode = request.get("apartCode");
        System.out.println("apartCode= " + apartCode);
        Apart apart = facilityService.findApartByCode(apartCode);
        System.out.println(apart);

        if (apart != null)
            return new ResponseEntity<String>(apart.getAddress(), HttpStatus.OK);
        else
            return new ResponseEntity<String>("fail", HttpStatus.OK);
    }


    //시스템 회원 가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinApartMember(@RequestBody @Valid JoinMemberDto joinMemberDto) throws Exception {

        System.out.println("joinMemberDto = " + joinMemberDto);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        ApartMember apartMember = new ApartMember();
        apartMember.setName(joinMemberDto.getName());
        apartMember.setLoginId(joinMemberDto.getLoginId());
        apartMember.setPassword(passwordEncoder.encode(joinMemberDto.getPassword())); //암호화필요
        apartMember.setPhone(joinMemberDto.getPhone());
        apartMember.setApart(facilityService.findApartByCode(joinMemberDto.getApartCode()));  //아파트 조회해와야 함
        apartMember.setAddressNumber(joinMemberDto.getAddressNumber());
        apartMember.setSystem(true);
        apartMember.setCreatedAt(LocalDateTime.now());
        apartMember.setRole(Role.APART_MEMBER);
        apartMember.setState(true);

        try {
            Integer id = memberService.joinApartMember(apartMember);
            if (id != null) {
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", "fail");
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("회원가입 실패 실패 : {}", e);
            resultMap.put("message", "fail");
            resultMap.put("excetpion", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
            return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login( @RequestBody Map<String, String> request) throws Exception {

        String loginId = request.get("loginId");
        String password = request.get("password");

        System.out.println("loginId= " + loginId + ", password= " + password);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {

            Member loginUser = memberService.findByLoginId(loginId);
            if (!passwordEncoder.matches(password, loginUser.getPassword())) {
                throw new Exception("비밀번호가 일치하지 않습니다.");
            }
            if (loginUser != null) {
                List<String> roles = new ArrayList<>();
                roles.add(loginUser.getRole().toString());
                String token = jwtTokenProvider.createToken(String.valueOf(loginUser.getId()), roles);
                logger.debug("로그인 토큰정보 : {}", token);
                resultMap.put("access-token", token);
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", "fail");
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


}
