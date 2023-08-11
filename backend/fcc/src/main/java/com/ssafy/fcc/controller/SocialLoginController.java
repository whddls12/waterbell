package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.PlatformType;
import com.ssafy.fcc.domain.member.Role;
import com.ssafy.fcc.dto.ApartMemberResponse;
import com.ssafy.fcc.dto.JoinMemberDto;
import com.ssafy.fcc.dto.SocialTempDto;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.MemberService;
import com.ssafy.fcc.service.SocialLoginServide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginServide socialLoginServide;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final FacilityService facilityService;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naver_client;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naver_secret;

    /**
     * 카카오 로그인 API
     * [GET] /app/login/kakao
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("login/oauth2/code/kakao")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam(required = false) String code) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            System.out.println("===========code: "+code +"======================================================");
            // URL에 포함된 code를 이용하여 액세스 토큰 발급
            String accessToken = socialLoginServide.getKakaoAccessToken(code);
            System.out.println(accessToken);

            // 액세스 토큰을 이용하여 카카오 서버에서 유저 정보(닉네임, 이메일) 받아오기
            HashMap<String, Object> userInfo = socialLoginServide.getUserInfoKakao(accessToken);
            System.out.println("login Controller : " + userInfo);

//            resultMap.put("userInfo",userInfo);

            String email = userInfo.get("email").toString();
            String name = userInfo.get("nickname").toString();

            //emil을 id로 가지는 유저가 없으면 회원가입
            System.out.println( "email= "+email);
            System.out.println( "name= "+name);

            if (memberService.validationDuplicateId(userInfo.get("email").toString())){
                resultMap.put("type", "join");

                try {
                    SocialTempDto answerTmp =socialLoginServide.socialTempSave(new SocialTempDto(email, name, PlatformType.KAKAO.toString()) );
                    if(answerTmp!=null) {
                        System.out.println("================레디스에 임시 저장하기 성공======================");
                        System.out.println(answerTmp);
                    resultMap.put("message", "success");
                    resultMap.put("key", answerTmp.getEmail());
                    }
                    else resultMap.put("message", "fail");

                }catch (Exception e){
                    resultMap.put("message", "fail");
                    resultMap.put("exception", e.getMessage());
                }

            }else{
                resultMap.put("type", "login");

                try {
                ApartMemberResponse apartMemberResponse = socialLoginServide.socialLogin(email);
                apartMemberResponse.setPlatformType(PlatformType.KAKAO);
                apartMemberResponse.setPlatformToken(accessToken);
                resultMap.put("member", apartMemberResponse);
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;

            }catch(Exception e){
                    resultMap.put("exception", e.getMessage());
                    resultMap.put("message", "fail");
                }
        }
        } catch (Exception exception) {
            resultMap.put("message", "fail");
            resultMap.put(" exception", exception.getMessage());
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @PostMapping("/social/join/{key}")
    public ResponseEntity<Map<String, Object>> joinApartMember(@RequestBody @Valid JoinMemberDto joinMemberDto, @PathVariable String key) throws Exception {

        System.out.println("joinMemberDto = " + joinMemberDto);
        System.out.println("key = " + key);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        SocialTempDto socialTempDto = socialLoginServide.getTempMember(key);

        ApartMember apartMember = new ApartMember();
        apartMember.setName(socialTempDto.getName());
        apartMember.setLoginId(socialTempDto.getEmail());
        apartMember.setPassword(passwordEncoder.encode(joinMemberDto.getPassword() + secret)); //암호화필요
        apartMember.setPhone(joinMemberDto.getPhone());
        apartMember.setApart(facilityService.findApartByCode(joinMemberDto.getApartCode()));  //아파트 조회해와야 함
        apartMember.setAddressNumber(joinMemberDto.getAddressNumber());
        apartMember.setSystem(false);
        apartMember.setCreatedAt(LocalDateTime.now());
        apartMember.setRole(Role.APART_MEMBER);
        apartMember.setState(true);
        if (socialTempDto.getType().equals(PlatformType.KAKAO.toString()))
            apartMember.setPlatformType(PlatformType.KAKAO);
        else if (socialTempDto.getType().equals(PlatformType.NAVER.toString()))
            apartMember.setPlatformType(PlatformType.NAVER);

        System.out.println("apartMember= "+apartMember);
        try {
            Integer id = memberService.joinApartMember(apartMember);
            if (id != null) {
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;
                socialLoginServide.removeTempMember(apartMember.getLoginId());
            } else {
                resultMap.put("message", "fail");
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("excetpion", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

//    @GetMapping("/social/join/{key}")
//    public ResponseEntity<Map<String, Object>> getSocialTemp(@RequestParam("key") String key) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//        try {
//            SocialTempDto socialTempDto = socialLoginServide.getTempMember(key);
//            if (socialTempDto == null) {
//
//                status = HttpStatus.BAD_REQUEST;
//                resultMap.put("message", "fail");
//            }
//        }catch(Exception e){
//
//        }
//
//
//
//    }

//    @ResponseBody
    @RequestMapping(value = "/login/oauth2/code/naver")
    public ResponseEntity<Map<String, Object>> naverLogin(@RequestParam("code") String code, @RequestParam("state") String state) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            // URL에 포함된 code를 이용하여 액세스 토큰 발급
            String accessToken = socialLoginServide.getNaverAccessToken(code,state);
            System.out.println(accessToken);

            // 액세스 토큰을 이용하여 카카오 서버에서 유저 정보(닉네임, 이메일) 받아오기
            HashMap<String, Object> userInfo = socialLoginServide.getUserInfoNaver(accessToken);
            System.out.println("login Controller : " + userInfo);

//            resultMap.put("userInfo",userInfo);

            String email = userInfo.get("email").toString();
            String name = userInfo.get("nickname").toString();

            //emil을 id로 가지는 유저가 없으면 회원가입
            System.out.println( "email= "+email);
            System.out.println( "name= "+name);

            if (memberService.validationDuplicateId(userInfo.get("email").toString())){
                resultMap.put("type", "join");

                try {
                    SocialTempDto answerTmp =socialLoginServide.socialTempSave(new SocialTempDto(email, name, PlatformType.NAVER.toString()) );
                    if(answerTmp!=null) {
                        System.out.println("================레디스에 임시 저장하기 성공======================");
                        System.out.println(answerTmp);
                        resultMap.put("message", "success");
                        resultMap.put("key", answerTmp.getEmail());
                    }
                    else resultMap.put("message", "fail");

                }catch (Exception e){
                    resultMap.put("message", "fail");
                    resultMap.put("exception", e.getMessage());
                }

            }else{
                resultMap.put("type", "login");

                try {
                    ApartMemberResponse apartMemberResponse = socialLoginServide.socialLogin(email);
                    apartMemberResponse.setPlatformType(PlatformType.NAVER);
                    apartMemberResponse.setPlatformToken(accessToken);
                    resultMap.put("member", apartMemberResponse);
                    resultMap.put("message", "success");
                    status = HttpStatus.ACCEPTED;

                }catch(Exception e){
                    resultMap.put("exception", e.getMessage());
                    resultMap.put("message", "fail");
                }
            }
        } catch (Exception exception) {
            resultMap.put("message", "fail");
            resultMap.put(" exception", exception.getMessage());
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}
