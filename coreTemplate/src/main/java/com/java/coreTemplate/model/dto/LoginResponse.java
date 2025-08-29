package com.java.coreTemplate.model.dto;

public record LoginResponse(Long userId, String username, String email, String message) {
}
