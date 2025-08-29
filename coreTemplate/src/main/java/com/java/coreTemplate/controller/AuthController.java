package com.java.coreTemplate.controller;

import com.java.coreTemplate.config.JwtTokenUtil;
import com.java.coreTemplate.config.OAuthService;
import com.java.coreTemplate.config.SessionAuthService;
import com.java.coreTemplate.model.dto.RegistrationRequest;
import com.java.coreTemplate.model.entity.UserDetails;
import com.java.coreTemplate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private OAuthService oauthService;
    @Autowired
    private SessionAuthService sessionAuthService;
    @Autowired
    private UserService userService;
    @GetMapping("/oauth/callback")
    public String oauthCallback(@RequestParam String code, HttpServletRequest request) {
        String accessToken = oauthService.getAccessToken(code);
        Map<String, Object> userInfo = oauthService.getUserInfo(accessToken);

        // Create session
        String userId = (String) userInfo.get("email"); // or other unique identifier
        String sessionId = sessionAuthService.createSession(userId, request);

        // Also generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userId);
        claims.put("name", userInfo.get("name"));
        String jwtToken = JwtTokenUtil.generateToken(userId, claims);

        return "Session ID: " + sessionId + "\nJWT Token: " + jwtToken;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {
        // Validate credentials (in a real app, check against database)
        if (!"admin".equals(username) || !"password".equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // Create session
        String sessionId = sessionAuthService.createSession(username, request);

        // Generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");
        String jwtToken = JwtTokenUtil.generateToken(username, claims);

        return "Session ID: " + sessionId + "\nJWT Token: " + jwtToken;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            UserDetails registeredUser = userService.registerUser(
                    registrationRequest.getUsername(),
                    registrationRequest.getPassword(),
                    registrationRequest.getEmail()
            );
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/userlogin")
    public ResponseEntity<?> loginUser(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {
        // Validate credentials (in a real app, check against database)
        Optional<UserDetails> authenticatedUser = userService.authenticateUser(
                username,
                password
        );
        if (authenticatedUser.isPresent()) {
            // Create session
            String sessionId = sessionAuthService.createSession(username, request);

            // Generate JWT token
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "user");
            String jwtToken = JwtTokenUtil.generateToken(username, claims);

            return new ResponseEntity<>("Session ID: " + sessionId + "\nJWT Token: " + jwtToken, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Username and password", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionAuthService.invalidateSession(session.getId(), request);
        }
        return "Logged out successfully";
    }
}