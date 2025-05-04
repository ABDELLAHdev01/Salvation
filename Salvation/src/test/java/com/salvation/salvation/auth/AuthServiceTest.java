package com.salvation.salvation.auth;

import com.salvation.salvation.dto.AuthenticateRequest;
import com.salvation.salvation.dto.AuthenticationResponse;
import com.salvation.salvation.dto.RegisterRequest;
import com.salvation.salvation.communs.exceptions.InvalidRefreshTokenException;
import com.salvation.salvation.communs.exceptions.UsernameAlreadyExistsException;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String ACCESS_TOKEN = "access.token.value";
    private static final String REFRESH_TOKEN = "refresh.token.value";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Common mock setups
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(jwtUtil.generateToken(any(User.class))).thenReturn(ACCESS_TOKEN);
        when(jwtUtil.generateRefreshToken(any(User.class))).thenReturn(REFRESH_TOKEN);
    }

    @Test
    void registerShouldCreateNewUserAndReturnTokens() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(TEST_USERNAME);
        registerRequest.setPassword(TEST_PASSWORD);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AuthenticationResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());

        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any(User.class));
        verify(jwtUtil).generateRefreshToken(any(User.class));
    }

    @Test
    void registerShouldThrowExceptionWhenUsernameExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(TEST_USERNAME);
        registerRequest.setPassword(TEST_PASSWORD);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateShouldReturnTokensWhenCredentialsAreValid() {
        // Arrange
        AuthenticateRequest request = new AuthenticateRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword(TEST_PASSWORD);

        User user = User.builder().username(TEST_USERNAME).password(ENCODED_PASSWORD).build();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        // Act
        AuthenticationResponse response = authService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(TEST_USERNAME, TEST_PASSWORD));
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(jwtUtil).generateToken(user);
        verify(jwtUtil).generateRefreshToken(user);
    }

    @Test
    void refreshShouldReturnNewAccessTokenWhenRefreshTokenIsValid() {
        // Arrange
        User user = User.builder().username(TEST_USERNAME).password(ENCODED_PASSWORD).build();
        
        when(jwtUtil.validateToken(REFRESH_TOKEN)).thenReturn(true);
        when(jwtUtil.extractUsername(REFRESH_TOKEN)).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        // Act
        AuthenticationResponse response = authService.refresh(REFRESH_TOKEN);

        // Assert
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());

        verify(jwtUtil).validateToken(REFRESH_TOKEN);
        verify(jwtUtil).extractUsername(REFRESH_TOKEN);
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(jwtUtil).generateToken(user);
    }

    @Test
    void refreshShouldThrowExceptionWhenRefreshTokenIsInvalid() {
        // Arrange
        when(jwtUtil.validateToken(REFRESH_TOKEN)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidRefreshTokenException.class, () -> {
            authService.refresh(REFRESH_TOKEN);
        });

        verify(jwtUtil).validateToken(REFRESH_TOKEN);
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(userRepository, never()).findByUsername(anyString());
        verify(jwtUtil, never()).generateToken(any(User.class));
    }
}