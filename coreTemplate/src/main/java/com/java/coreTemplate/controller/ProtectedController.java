package com.java.coreTemplate.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProtectedController {
    @GetMapping("/protected")
    public String protectedResource(HttpServletRequest request) {
        return "This is a protected resource. Your session is active.";
    }
}