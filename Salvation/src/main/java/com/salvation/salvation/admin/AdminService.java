package com.salvation.salvation.admin;

import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {

            logger.info("User not found with id: {}", id);
            return new IllegalArgumentException("User not found with id: " + id);
        });
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            logger.info("User not found with username: {}", username);
            return new IllegalArgumentException("User not found with username: " + username);
        });
    }
}
