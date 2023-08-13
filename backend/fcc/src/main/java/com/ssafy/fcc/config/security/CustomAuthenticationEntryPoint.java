package com.ssafy.fcc.config.security;

import com.ssafy.fcc.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint extends Throwable implements AuthenticationEntryPoint {  //정상적으로 jwt 토큰이 오지 않은 경우 예외를 잡아내기  위함

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");


        ErrorCode errorCode;
        if((request.getHeader("Authorization") == null) ){
            errorCode = ErrorCode.NON_LOGIN;
            setResponse(response, errorCode);
            return;
        }


        System.out.println("==========================================");
        System.out.println(request.getHeader("Authorization").toString());
        System.out.println(exception);


        log.debug("log: exception: {} ", exception);

        /**
         * 토큰 없는 경우
         */
        if(exception == null) {
            errorCode = ErrorCode.NON_LOGIN;
            setResponse(response, errorCode);
            return;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if(exception.equals(ErrorCode.INVALID_TOKEN.getCode())) {
            errorCode = ErrorCode.INVALID_TOKEN;
            setResponse(response, errorCode);
        }
    }


    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
                + "\", \"code\" : \"" +  errorCode.getCode()
                + "\", \"status\" : " + errorCode.getStatus()
                + ", \"errors\" : [ ] }");
    }
}