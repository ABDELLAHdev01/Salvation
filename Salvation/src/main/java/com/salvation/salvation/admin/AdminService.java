package com.salvation.salvation.admin;

import com.salvation.salvation.communs.exceptions.ResourceNotFoundException;
import com.salvation.salvation.communs.helpers.Helpers;
import com.salvation.salvation.dto.UserDto;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    /**
     * Service class for managing admin operations on users.
     * It provides methods for retrieving, banning, unbanning, deleting,
     * and undeleting users based on their ID or username.
     */

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(UserDto::fromUser)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return userRepository.findByIdAndDeletedFalse(id).map(user -> {
            logger.info("User found with id: {}", id);
            return UserDto.fromUser(user);
        }).orElseThrow(() -> {
            logger.warn("User not found with id: {}", id);
            return new ResourceNotFoundException("User not found with id: " + id);
        });
    }

    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username).map(user -> {
            logger.info("User found with username: {}", username);
            return UserDto.fromUser(user);
        }).orElseThrow(() -> {
            logger.warn("User not found with username: {}", username);
            return new ResourceNotFoundException("User not found with username: " + username);
        });
    }

    private User getUserOrThrow(Optional<User> optionalUser, String identifier, boolean isId) {
        return optionalUser.orElseThrow(() -> {
            logger.warn("User not found with {}: {}", isId ? "id" : "username", identifier);
            return new ResourceNotFoundException("User not found with " + (isId ? "id" : "username") + ": " + identifier);
        });
    }

    private void updateUserEnabledStatus(User user, boolean enabled, String identifier, boolean isId) {
        user.setEnabled(enabled);
        userRepository.save(user);
        String action = enabled ? "unbanned" : "banned";
        logger.info("User with {} {} has been {}", isId ? "id" : "username", identifier, action);
    }

    @Transactional
    public void banUserById(Long id) {
        User user = getUserOrThrow(userRepository.findByIdAndDeletedFalse(id), Helpers.stringify(id), true);
        updateUserEnabledStatus(user, false, Helpers.stringify(id), true);
    }

    @Transactional
    public void banUserByUsername(String username) {
        User user = getUserOrThrow(userRepository.findByUsernameAndDeletedFalse(username), username, false);
        updateUserEnabledStatus(user, false, username, false);
    }

    @Transactional
    public void unbanUserById(Long id) {
        User user = getUserOrThrow(userRepository.findByIdAndDeletedFalse(id), Helpers.stringify(id), true);
        updateUserEnabledStatus(user, true, Helpers.stringify(id), true);
    }

    @Transactional
    public void unbanUserByUsername(String username) {
        User user = getUserOrThrow(userRepository.findByUsernameAndDeletedFalse(username), username, false);
        updateUserEnabledStatus(user, true, username, false);
    }

    private void softDeleteUser(User user, String identifier, boolean isId) {
        user.setDeleted(true);
        userRepository.save(user);
        logger.info("User with {} {} has been deleted", isId ? "id" : "username", identifier);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = getUserOrThrow(userRepository.findByIdAndDeletedFalse(id), Helpers.stringify(id), true);
        softDeleteUser(user, Helpers.stringify(id), true);
        logger.info("User with id {} has been deleted", id);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = getUserOrThrow(userRepository.findByUsernameAndDeletedFalse(username), username, false);
        softDeleteUser(user, username, false);
        logger.info("User with username {} has been deleted", username);
    }

    @Transactional
    public void unDeleteUserById(Long id) {
        User user = getUserOrThrow(userRepository.findByIdAndDeletedTrue(id), Helpers.stringify(id), true);
        user.setDeleted(false);
        userRepository.save(user);
        logger.info("User with id {} has been undeleted", id);
    }
    @Transactional
    public void unDeleteUserByUsername(String username) {
        User user = getUserOrThrow(userRepository.findByUsernameAndDeletedTrue(username), username, false);
        user.setDeleted(false);
        userRepository.save(user);
        logger.info("User with username {} has been undeleted", username);
    }
}
