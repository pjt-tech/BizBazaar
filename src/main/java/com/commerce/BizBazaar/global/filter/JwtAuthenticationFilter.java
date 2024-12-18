package com.commerce.BizBazaar.global.filter;

import com.commerce.BizBazaar.global.util.JwtUtil;
import com.commerce.BizBazaar.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Authorization 헤더에서 토큰 추출
        String token = extractTokenFromCookie(request);

        if (token != null && jwtUtil.validateToken(token)) {
            // 토큰에서 username 추출
            String username = jwtUtil.extractUsername(token);

            // DB에서 사용자 정보를 가져오는 대신, 이미 인증된 사용자가 있으면 SecurityContextHolder에 설정
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Authentication 객체 생성하여 SecurityContext에 설정
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 인증 정보를 SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }



    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();  // 쿠키에서 access_token 값 추출
                }
            }
        }
        return null;
    }
}

