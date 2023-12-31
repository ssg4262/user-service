package com.oxog.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxog.userservice.model.responseModel.userDto.UserModel;
import com.oxog.userservice.model.requestModel.user.RequestLogin;
import com.oxog.userservice.service.userSevice.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
@Log4j2
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager ,
                                UserService userService,
                                Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

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
            String userEmail = ((User)authResult.getPrincipal()).getUsername();
            UserModel userModel = userService.getUserByEmail(userEmail);
            if(userModel.getDeleteYn().equals("N")){
                String token = Jwts.builder()
                        .setSubject(userModel.getUserId())
                        .setExpiration(new Date(System.currentTimeMillis() +
                                Long.parseLong(env.getProperty("token.expiration_time")))) // yaml에 정의한 속성
                        .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                        .compact();

                response.addHeader("token" , token);
                response.addHeader("userId" , userModel.getUserId());
            }
    }
}
