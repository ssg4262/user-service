package com.oxog.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxog.userservice.model.requestModel.RequestLogin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException { // 인증 요청
        try {
            RequestLogin credits = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class); // 리퀘스트값 역직렬화로 가져오기
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credits.getEmail(),
                            credits.getPassword() ,
                            new ArrayList<>())
            );           // 이메일과 비밀번호를 토큰으로 new ArrayList<>() 엔 권한값전달

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // 로그인 성공시
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException { // 인증 성공시
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
