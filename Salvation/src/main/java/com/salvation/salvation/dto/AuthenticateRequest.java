package com.salvation.salvation.dto;

import lombok.Data;

@Data
public class AuthenticateRequest {
    private String username;
    private String password;
}
