package com.oxog.userservice.security;

import com.oxog.userservice.service.userSevice.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private  UserService userService;
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    private  ObjectPostProcessor<Object> objectPostProcessor;

    private static final String[] WHITE_LIST = {
            "/users/**",
            "/",
            "/**"
    };

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, ObjectPostProcessor<Object> objectPostProcessor) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectPostProcessor = objectPostProcessor;
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(new IpAddressMatcher("50.10.10.164")).permitAll()
                                .and()
                                .addFilter(getAuthenticationFilter());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        return http.build();
    }

    //userDetailsService db 패스워드와 로그인 시 입력된 데이터 비교
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return auth.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
        return authenticationFilter;
    }

}