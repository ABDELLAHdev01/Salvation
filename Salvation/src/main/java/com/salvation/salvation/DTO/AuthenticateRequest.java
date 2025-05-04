package com.salvation.salvation.DTO;

import lombok.Data;

@Data
public class AuthenticateRequest {
    private String username;
    private String password;
}
