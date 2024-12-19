package com.commerce.BizBazaar.global.config;

import com.commerce.BizBazaar.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자만 /admin/** 경로에 접근 가능
                .anyRequest().authenticated()  // 나머지 경로는 인증된 사용자만 접근 가능
                .and()
                .formLogin()
                .loginPage("/login")  // 로그인 페이지 설정
                .permitAll()
                .and()
                .logout()
                .permitAll();  // 로그아웃 설정

        return http.build();  // SecurityFilterChain 반환
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // PasswordEncoder 빈 등록
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);  // 사용자 정보 제공을 위한 서비스
    }
}