package com.ssafy.fcc.config.security;
import com.ssafy.fcc.dto.RefreshToken;
import com.ssafy.fcc.dto.TokenDto;
import com.ssafy.fcc.service.MemberService;
import io.jsonwebtoken.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증

    @Value("spring.jwt.secret") //application.properites에 저장
    private String secretKey; 

    private final long accessTokenValidMillisecond = 1000L * 60 * 60; // 1시간 토큰 유효

    private final  long refreshTokenValidMillisecond = 1000L * 60 * 60* 14; // 14일 토큰 유효

//    private long accessTokenValidMillisecond = 1000L*60*2 ; // TEST 토큰 유효
//    private final  long refreshTokenValidMillisecond = 1000L * 60 * 5; //test


    private final UserDetailsService userDetailsService;
    private final MemberService memberService;
    private final RedisTemplate redisTemplate;



    //키 초기화 //test
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public TokenDto createToken(String userPk, List<String> role) {
        //claims에 회원을 구분할 수 있는 값을 세팅
        Claims claims = Jwts.claims().setSubject(userPk);// 사용자 구분을 위해 pk넣어줌
        claims.put("role", role);
        Date now = new Date();

        String accessToken =  Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond)) // 토큰 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, 암호키
                .compact();

        String refreshToken =  Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).key(userPk).build();
    }

    // Jwt 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token)  {
//        Claims claims = parseClaim(token);
//        if(claims.get("roles") == null){
//            throw new CustomAuthenticationEntryPoint();
//        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구분 pk추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    private Claims parseClaim(String token){
        try{
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // Request의 Header에서 token 파싱
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            //token이 블랙리스트에 있는지 확인
            String isLogout = (String) redisTemplate.opsForValue().get(jwtToken);
            if (!ObjectUtils.isEmpty(isLogout)) {
                return false;
            }

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());


        } catch (Exception e) {
            return false;

        }
    }

    public String validateRefreshToken(RefreshToken refreshTokenObj){
        String refreshToken = refreshTokenObj.getRefreshToken();

        try {
            // 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성
            if (!claims.getBody().getExpiration().before(new Date())) {
                return recreationAccessToken(claims.getBody().getSubject().toString(), claims.getBody().get("roles"));
            }
        }catch (Exception e) {
            //검증에서 통과되지 않음, 시간 만료
            return null;
        }
        return null;
    }

    //refresh token으로 새로운 access token을 생성하여 반환
    public String recreationAccessToken(String pk, Object  role){

        Claims claims = Jwts.claims().setSubject(pk);
        claims.put("role", role);
        Date now = new Date();

        //Access Token
        String accessToken =  Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond)) // 토큰 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, 암호키
                .compact();

        return accessToken;
    }

    //토큰의 만료 날짜를 가져옴
    public Long getExpiration(String accessToken){

        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
        Date expiration = claims.getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }



}