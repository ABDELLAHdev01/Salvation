package com.salvation.salvation.auth;

import com.salvation.salvation.dto.AuthenticateRequest;
import com.salvation.salvation.dto.AuthenticationResponse;
import com.salvation.salvation.dto.RefreshTokenRequest;
import com.salvation.salvation.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.debug("Received registration request for user: {}", registerRequest.getUsername());
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticateRequest request) {
        logger.debug("Received login request for user: {}", request.getUsername());
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        logger.debug("Received token refresh request");
        return ResponseEntity.ok(authService.refresh(refreshTokenRequest.getRefreshToken()));
    }
}
