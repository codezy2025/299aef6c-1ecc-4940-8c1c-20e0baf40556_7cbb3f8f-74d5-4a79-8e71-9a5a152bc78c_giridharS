package com.java.coreTemplate.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RegistrationRequest {
        private String username;
        private String password;
        private String email;
}