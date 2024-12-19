package com.commerce.BizBazaar.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // API 요청을 위해 CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/api/auth/login")  // API 엔드포인트 변경
                        .successHandler((request, response, authentication) -> {
                            // 성공 시 리다이렉트
                            response.sendRedirect("/admin/dashboard");
                        })
                        .failureHandler((request, response, exception) -> {
                            log.error("Authentication failed: {}", exception.getMessage());
                            String errorMessage = "";
                            if (exception instanceof BadCredentialsException) {
                                errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
                            } else if (exception instanceof DisabledException) {
                                errorMessage = "계정이 비활성화되었습니다.";
                            } else if (exception instanceof LockedException) {
                                errorMessage = "계정이 잠겼습니다.";
                            } else {
                                errorMessage = "로그인에 실패했습니다.";
                            }
                            // 오류 메시지를 request에 설정
                            request.setAttribute("error", errorMessage);

                            // 로그인 페이지로 포워딩
                            request.getRequestDispatcher("/").forward(request, response);  // 로그인 페이지로 포워딩
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}