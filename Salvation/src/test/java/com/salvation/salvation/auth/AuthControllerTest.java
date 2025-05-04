package com.salvation.salvation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salvation.salvation.dto.AuthenticateRequest;
import com.salvation.salvation.dto.AuthenticationResponse;
import com.salvation.salvation.dto.RegisterRequest;
import com.salvation.salvation.communs.exceptions.InvalidRefreshTokenException;
import com.salvation.salvation.communs.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.TestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Configuration
    static class TestConfig {
        @Bean
        public AuthService authService() {
            return org.mockito.Mockito.mock(AuthService.class);
        }
    }

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password";
    private static final String ACCESS_TOKEN = "access.token.value";
    private static final String REFRESH_TOKEN = "refresh.token.value";

    private AuthenticationResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = AuthenticationResponse.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();
    }

    @Test
    void registerShouldReturnTokensWhenSuccessful() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(TEST_USERNAME);
        registerRequest.setPassword(TEST_PASSWORD);

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN));
    }

    @Test
    void registerShouldReturnBadRequestWhenUsernameExists() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(TEST_USERNAME);
        registerRequest.setPassword(TEST_PASSWORD);

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new UsernameAlreadyExistsException("Username already exists"));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValid() throws Exception {
        // Arrange
        AuthenticateRequest loginRequest = new AuthenticateRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);

        when(authService.authenticate(any(AuthenticateRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN));
    }

    @Test
    void refreshShouldReturnNewAccessTokenWhenRefreshTokenIsValid() throws Exception {
        // Arrange
        when(authService.refresh(REFRESH_TOKEN)).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REFRESH_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN));
    }

    @Test
    void refreshShouldReturnUnauthorizedWhenRefreshTokenIsInvalid() throws Exception {
        // Arrange
        when(authService.refresh(REFRESH_TOKEN))
                .thenThrow(new InvalidRefreshTokenException("Invalid refresh token"));

        // Act & Assert
        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(REFRESH_TOKEN))
                .andExpect(status().isUnauthorized());
    }
}
