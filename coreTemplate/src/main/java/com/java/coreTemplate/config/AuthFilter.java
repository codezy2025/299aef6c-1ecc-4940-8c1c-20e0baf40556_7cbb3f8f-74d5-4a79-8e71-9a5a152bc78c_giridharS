package com.java.coreTemplate.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
    private final SessionAuthService sessionAuthService;

    public AuthFilter(SessionAuthService sessionAuthService) {
        this.sessionAuthService = sessionAuthService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check for JWT token in header
        String jwtToken = httpRequest.getHeader("Authorization");

        // Check for session cookie
        String sessionId = httpRequest.getSession(false) != null ?
                httpRequest.getSession(false).getId() : null;

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            // JWT-based authentication
            String token = jwtToken.substring(7);
            if (JwtTokenUtil.validateToken(token)) {
                chain.doFilter(request, response);
                return;
            }
        } else if (sessionId != null && sessionAuthService.isValidSession(sessionId)) {
            // Session-based authentication
            chain.doFilter(request, response);
            return;
        }

        // Authentication failed
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}