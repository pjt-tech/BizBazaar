package com.commerce.BizBazaar.global.config;

import com.commerce.BizBazaar.global.filter.JwtAuthenticationFilter;
import com.commerce.BizBazaar.global.util.JwtUtil;
import com.commerce.BizBazaar.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);  // Custom UserDetailsService 사용
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정
                .authorizeRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // 로그인, 회원가입은 인증 없이 접근 가능
                        .requestMatchers("/register", "/css/**").permitAll()  // 메인 페이지 및 정적 리소스 접근 허용
                        .requestMatchers("/vendor/**").hasRole("VENDOR")  // VENDOR 권한이 있는 사용자만 접근 가능
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")  // CUSTOMER 권한이 있는 사용자만 접근 가능
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/")  // 로그인 페이지 URL 설정
                        .permitAll()  // 로그인 페이지는 인증 없이 접근 가능
                        .defaultSuccessUrl("/", true)  // 로그인 성공 후 홈 화면으로 리다이렉트
                        .failureUrl("/auth/login/fail?error=true")  // 로그인 실패 시 에러 메시지
                )
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가하여 JWT 인증을 먼저 수행하도록 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));  // 허용할 도메인 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 허용할 HTTP 메서드 설정
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));  // 허용할 헤더 설정
        configuration.setAllowCredentials(true);  // 인증 정보 허용 (쿠키 등)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        return source;
    }
}