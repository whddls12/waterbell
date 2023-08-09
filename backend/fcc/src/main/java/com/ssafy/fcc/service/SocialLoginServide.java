package com.ssafy.fcc.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssafy.fcc.config.security.JwtTokenProvider;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.dto.ApartMemberResponse;
import com.ssafy.fcc.dto.RefreshToken;
import com.ssafy.fcc.dto.SocialTempDto;
import com.ssafy.fcc.dto.TokenDto;
import com.ssafy.fcc.repository.MemberRepository;
import com.ssafy.fcc.repository.SocialRedisRepository;
import com.ssafy.fcc.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialLoginServide {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private  String kakaoClienetSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private  String kakaoRedirectUri;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final SocialRedisRepository socialRedisRepository;


    @Value("${spring.jwt.secret}")
    private String secret;


    public String getKakaoAccessToken (String code) {

        String accessToken = "";
        String refreshToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {

            System.out.println("kakaoClientId= "+ kakaoClientId);
            System.out.println(kakaoClienetSecret);
            System.out.println("kakaoRedirectUri= "+kakaoRedirectUri);

            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id="+kakaoClientId+ // REST_API_KEY
//                    "&redirect_uri=http://i9b101.p.ssafy.io:8080/login/oauth2/code/kakao" + // REDIRECT_URI
                    "&redirect_uri=http://localhost:8081/auth/kakao" + // REDIRECT_URI
                    "&code=" + code;
            System.out.println("========================================");
            bufferedWriter.write(sb);
            System.out.println("========================================");
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public HashMap<String, Object> getUserInfoKakao(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String postURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();


            System.out.println("=========가져온 kakao user 정보=============");
            System.out.println(email);
            System.out.println(nickname);

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return userInfo;
    }


    @Transactional
    public SocialTempDto socialTempSave(SocialTempDto m) throws Exception{

        socialRedisRepository.save(m);

        System.out.println("저장 완료");
        return socialRedisRepository.get(m.getEmail());
    }




    public ApartMemberResponse socialLogin(String email) throws Exception{

        Member loginMember = memberRepository.findByLoginId(email);

        if (loginMember == null) {
            throw new Exception("일치하는 회원이 없습니다.");
        }

        List<String> roles = new ArrayList<>();
        roles.add(loginMember.getRole().toString());
        TokenDto token = jwtTokenProvider.createToken(String.valueOf(loginMember.getId()), roles);
        System.out.println(token);

        //타입이 이게 아니라면 회원정보 잘못됨
        ApartMemberResponse apartMemberRespose = new ApartMemberResponse((ApartMember) loginMember, token);
        tokenRepository.save(new RefreshToken(loginMember.getId(), token.getRefreshToken()));

        return apartMemberRespose;
    }


    @Transactional
    public SocialTempDto getTempMember(String email) {
        return socialRedisRepository.get(email);
    }

    @Transactional
    public void removeTempMember(String email){
        socialRedisRepository.deleteById(email);
    }



    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naver_client;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naver_secret;


    public String getNaverAccessToken (String code, String state) {

        String accessToken = "";
        String refreshToken = "";
        String requestURL = "https://nid.naver.com/oauth2.0/token";

        try {

            System.out.println(naver_client);
            System.out.println(naver_secret);

            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            // setDoOutput()은 OutputStream으로 POST 데이터를 넘겨 주겠다는 옵션이다.
            // POST 요청을 수행하려면 setDoOutput()을 true로 설정한다.
            conn.setDoOutput(true);

            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id="+naver_client+ // REST_API_KEY
                    "&client_secret="+naver_secret+ // REST_API_KEY
                    "&state="+state+
                    "&code=" + code;
            bufferedWriter.write(sb);
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public HashMap<String, Object> getUserInfoNaver(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String postURL = "https://openapi.naver.com/v1/nid/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());
            JsonObject properties = element.getAsJsonObject().get("response").getAsJsonObject();
//            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String email = properties.getAsJsonObject().get("email").getAsString();
            String nickname = properties.getAsJsonObject().get("name").getAsString();


//            System.out.println("=========가져온 Naver user 정보=============");
//            System.out.println(email);
//            System.out.println(nickname);

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return userInfo;
    }



}