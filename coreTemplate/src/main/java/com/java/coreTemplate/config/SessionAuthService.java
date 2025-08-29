package com.java.coreTemplate.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionAuthService {
    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

    public String createSession(String userId, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", userId);
        activeSessions.put(session.getId(), userId);
        return session.getId();
    }

    public boolean isValidSession(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public void invalidateSession(String sessionId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        activeSessions.remove(sessionId);
    }

    public String getUserIdFromSession(String sessionId) {
        return activeSessions.get(sessionId);
    }
}