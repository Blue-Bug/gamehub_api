package com.bluebug.gamehub.gamehub_core.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized Error, Message : {}",authException.getMessage());
        log.error("Unauthorized Error, Message : {}",request.getAttribute("Unauthorized"));

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,(String)request.getAttribute("Unauthorized"));
    }
}
