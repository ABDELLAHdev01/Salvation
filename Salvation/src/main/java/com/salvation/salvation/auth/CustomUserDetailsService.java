package com.salvation.salvation.auth;

import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userDetails", key = "#username")
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user details for username: {}", username);
        
        try {
            // Retrieve user from the database based on the username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            logger.debug("User found: {}", username);
            // Return a CustomUserDetails object, which contains user data
            return new CustomUserDetails(user);
        } catch (UsernameNotFoundException e) {
            logger.warn("Failed to load user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error loading user: {}", e.getMessage(), e);
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage(), e);
        }
    }
}