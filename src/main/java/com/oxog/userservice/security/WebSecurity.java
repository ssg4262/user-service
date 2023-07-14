package com.oxog.userservice.security;

import com.oxog.userservice.service.userSevice.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
    private Environment env;
    private  ObjectPostProcessor<Object> objectPostProcessor;

    private static final String[] WHITE_LIST = {
            "/users/**",
            "/actuator/**",
            "/",
            "/**"
    };

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env, ObjectPostProcessor<Object> objectPostProcessor) {
        this.userService = userService;
        this.env = env;
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
                                .addFilter(getAuthenticationFilter()); // AuthenticationFilter 실행
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
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(builder),userService,env);
        return authenticationFilter;
    }

}