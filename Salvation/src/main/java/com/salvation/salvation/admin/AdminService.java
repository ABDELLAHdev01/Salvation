package com.salvation.salvation.admin;

import com.salvation.salvation.communs.exceptions.ResourceNotFoundException;
import com.salvation.salvation.dto.UserDto;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            logger.info("User found with id: {}", id);
            return UserDto.fromUser(user);
        }).orElseThrow(() -> {
            logger.warn("User not found with id: {}", id);
            return new ResourceNotFoundException("User not found with id: " + id);
        });
    }

    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> {
            logger.info("User found with username: {}", username);
            return UserDto.fromUser(user);
        }).orElseThrow(() -> {
            logger.warn("User not found with username: {}", username);
            return new ResourceNotFoundException("User not found with username: " + username);
        });
    }

    private void setUserEnabledStatus(Optional<User> optionalUser, boolean enabled, String identifier, boolean isId) {
        optionalUser.ifPresentOrElse(user -> {
            user.setEnabled(enabled);
            userRepository.save(user);
            String action = enabled ? "unbanned" : "banned";
            logger.info("User with {} {} has been {}", isId ? "id" : "username", identifier, action);
        }, () -> {
            logger.warn("User not found with {}: {}", isId ? "id" : "username", identifier);
            throw new ResourceNotFoundException("User not found with " + (isId ? "id" : "username") + ": " + identifier);
        });
    }

    public void banUserById(Long id) {
        setUserEnabledStatus(userRepository.findById(id), false, String.valueOf(id), true);
    }

    public void banUserByUsername(String username) {
        setUserEnabledStatus(userRepository.findByUsername(username), false, username, false);
    }

    public void unbanUserById(Long id) {
        setUserEnabledStatus(userRepository.findById(id), true, String.valueOf(id), true);
    }

    public void unbanUserByUsername(String username) {
        setUserEnabledStatus(userRepository.findByUsername(username), true, username, false);
    }
}
