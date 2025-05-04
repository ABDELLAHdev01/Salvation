package com.salvation.salvation.admin;

import com.salvation.salvation.dto.UserDto;
import com.salvation.salvation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUser)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(user -> {
            logger.warn("User found with id: {}", id);
            return UserDto.fromUser(user);
        }).orElseThrow(() -> {
            logger.warn("User not found with id: {}", id);
            return new NoSuchElementException("User not found with id: " + id);
        });
    }

    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> {
            logger.warn("User found with username: {}", username);
            return UserDto.fromUser(user);
        }).orElseThrow(() -> {
            logger.warn("User not found with username: {}", username);
            return new NoSuchElementException("User not found with username: " + username);
        });
    }
}
