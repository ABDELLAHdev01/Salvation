package com.salvation.salvation.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRespone {
    private String token;
    private String username;
    private String role;

}
