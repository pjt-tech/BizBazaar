package com.commerce.BizBazaar.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // /auth/** 경로는 인증 없이 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자만 접근 가능
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 자원 허용
                        .anyRequest().authenticated()  // 나머지 경로는 인증된 사용자만 접근 가능
                )
                .formLogin(form -> form
                        .loginPage("/")  // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/admin/dashboard")  // 로그인 성공 시 기본 경로
                        .failureUrl("/login?error=true")  // 로그인 실패 시 이동 경로
                        .permitAll()  // 모든 사용자 접근 가능
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 요청 경로
                        .logoutSuccessUrl("/login?logout=true")  // 로그아웃 성공 후 이동 경로
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")  // JSESSIONID 쿠키 삭제
                )
                .csrf(AbstractHttpConfigurer::disable);  // CSRF 비활성화 (필요 시 활성화)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // PasswordEncoder 빈 등록
    }
}