package com.salvation.salvation.controller;

import com.salvation.salvation.dto.CharacterRequest;
import com.salvation.salvation.dto.UserDto;
import com.salvation.salvation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-character")
    public ResponseEntity<UserDto> createCharacter(@RequestBody CharacterRequest request) {
        return ResponseEntity.ok(userService.createCharacter(request));

    }

}