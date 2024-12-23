package com.commerce.BizBazaar.global.config;

import com.commerce.BizBazaar.global.handler.AuthenticationFailureHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFailureHandlerImpl authenticationFailureHandler;

    // 생성자를 통해 AuthenticationFailureHandler 주입
    public SecurityConfig(AuthenticationFailureHandlerImpl authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // API 요청을 위해 CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/css/**", "/static/js/**", "/images/**").permitAll()
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
                        .failureHandler(authenticationFailureHandler)
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