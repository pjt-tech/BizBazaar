package com.commerce.BizBazaar.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        }
        else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화되었습니다.";
        }
        else if (exception instanceof LockedException) {
            errorMessage = "계정이 잠겼습니다.";
        }
        else {
            errorMessage = "로그인에 실패했습니다.";
        }

        // 세션에 에러 메시지 저장
        HttpSession session = request.getSession();
        session.setAttribute("loginError", errorMessage);

        response.sendRedirect("/?error=true");
    }
}
