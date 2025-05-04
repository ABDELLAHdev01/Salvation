package com.salvation.salvation.auth;

import com.salvation.salvation.dto.AuthenticateRequest;
import com.salvation.salvation.dto.AuthenticationResponse;
import com.salvation.salvation.dto.RegisterRequest;
import com.salvation.salvation.communs.exceptions.InvalidRefreshTokenException;
import com.salvation.salvation.communs.exceptions.UsernameAlreadyExistsException;
import com.salvation.salvation.model.Role;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.RoleRepository;
import com.salvation.salvation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        logger.debug("Registering new user: {}", registerRequest.getUsername());

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            logger.warn("Registration failed: Username already exists: {}", registerRequest.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    logger.info("ROLE_USER not found, creating a new one.");
                    return roleRepository.save(Role.builder().name("ROLE_USER").build());
                });

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(encodedPassword)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .roles(Collections.singleton(role))
                .credentialsNonExpired(true)
                .build();

        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getUsername());

        return generateAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        logger.debug("Authenticating user: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));

            logger.info("User authenticated successfully: {}", user.getUsername());
            return generateAuthResponse(user);

        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {}", request.getUsername());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse refresh(String refreshToken) {
        logger.debug("Processing refresh token");

        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            String newAccessToken = jwtUtil.generateToken(user);
            logger.info("Access token refreshed for user: {}", username);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken) // Reuse the same refresh token
                    .build();
        } else {
            logger.warn("Invalid refresh token");
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
    }

    @Cacheable(value = "userByUsername", key = "#username")
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private AuthenticationResponse generateAuthResponse(User user) {
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
