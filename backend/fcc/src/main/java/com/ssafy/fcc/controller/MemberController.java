package com.ssafy.fcc.controller;


import com.ssafy.fcc.config.security.CustomUserDetail;
import com.ssafy.fcc.config.security.JwtTokenProvider;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.member.*;
import com.ssafy.fcc.dto.*;
import com.ssafy.fcc.repository.TokenRepository;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final FacilityService facilityService;
    private final JwtTokenProvider jwtTokenProvider; // jwt 토큰 생성
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final RedisTemplate redisTemplate;

    //아이디 중복 조회
    @GetMapping("/join/duplication/{loginId}")
    public ResponseEntity<String> checkDuplicationLoginId(@PathVariable String loginId) {

        if (memberService.validationDuplicateId(loginId))
            return new ResponseEntity<String>("success", HttpStatus.OK);
        else
            return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
    }


    //아파트 코드 - 주소
    @PostMapping("/join/validationApartCode")
    public ResponseEntity<Map<String, Object>> checkValidationApartCode(@RequestBody Map<String, String> request) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        String apartCode = request.get("apartCode");
        System.out.println("apartCode= " + apartCode);
        Apart apart = facilityService.findApartByCode(apartCode);
        System.out.println(apart);

        if (apart != null) {
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
            resultMap.put("address", apart.getAddress());
        } else {
            resultMap.put("message", "fail");
            status = HttpStatus.ACCEPTED;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);

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
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) throws Exception {

        String loginId = request.get("loginId");
        String password = request.get("password");


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
                TokenDto token = jwtTokenProvider.createToken(String.valueOf(loginUser.getId()), roles);
                logger.debug("로그인 토큰정보 : {}", token);

                System.out.println(loginUser);


                if (loginUser.getRole() == Role.APART_MEMBER) {
                    ApartMemberRespose apartMemberRespose = new ApartMemberRespose((ApartMember) loginUser, token);
                    resultMap.put("member", apartMemberRespose);
                } else if (loginUser.getRole() == Role.APART_MANAGER) {
                    ApartManagerResponse apartManagerResponse = new ApartManagerResponse((ApartManager) loginUser, token);
                    resultMap.put("member", apartManagerResponse);
                } else if (loginUser.getRole() == Role.PUBLIC_MANAGER) {

                    PublicManagerResponse publicManagerResponse = new PublicManagerResponse((PublicManager) loginUser, token);
                    resultMap.put("member", publicManagerResponse);
                }

                //redis에 저장
                tokenRepository.save(new RefreshToken(loginUser.getId(), token.getRefreshToken()));

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


    @GetMapping("/findMember/token")
    public ResponseEntity<Map<String, Object>> findMember() throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        System.out.println(id);
        if (id == null || id.equals("") || id.equals("anonymousUser")) {
            resultMap.put("message", "fail");
        } else {
            Member findMember = memberService.findById(Integer.parseInt(id));
            resultMap.put("message", "success");
            resultMap.put("member", memberService.getMemberResponse(findMember, ""));

        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    @GetMapping("/findUser")
    public ResponseEntity<UserDetails> getUserInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // authentication 객체로부터 사용자 정보를 가져와서 리턴
        // 여기서 userDetails에는 CustomUserDetail에 담긴 사용자 정보가 있습니다.

        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(@RequestBody RefreshToken refreshTokenObj) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        System.out.println("==============refresh token==============: " + refreshTokenObj);

        String newAccessToken = jwtTokenProvider.validateRefreshToken(refreshTokenObj);
        if (newAccessToken != null) {
            TokenDto tokenDto = TokenDto.builder().accessToken(newAccessToken).build();
            resultMap.put("message", "success");
            resultMap.put("accessToken", tokenDto.getAccessToken());
        } else {
            // refresh token이 유효하지 않거나 만료됨
            resultMap.put("message", "fail");
            resultMap.put("exception", "refresh token이 유효하지 않거나 만료됨");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest servletRequest) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 1. Access Token 검증
            String accessToken = jwtTokenProvider.resolveToken(servletRequest);
            if (!jwtTokenProvider.validateToken(accessToken)) {
                resultMap.put("message", "fail");
                resultMap.put("error", "유효하지 않은 토큰입니다.");
                return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
            }

            // 2. Refresh Token이 있는지 확인 후 있을 경우 삭제
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            String id = authentication.getName();
            if (tokenRepository.findById(id).isPresent()) {
                tokenRepository.deleteById(id);
            }

            // 3. 해당 Access Token 유효시간을 가져와서 Blacklist로 저장하기
            Long expiration = jwtTokenProvider.getExpiration(accessToken);
            redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);


            // 로그아웃 성공 응답 처리
            resultMap.put("message", "success");

        } catch (Exception e) {
            resultMap.put("message", "fail");
        }
        return ResponseEntity.ok(resultMap);

    }

}
