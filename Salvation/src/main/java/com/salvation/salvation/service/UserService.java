package com.salvation.salvation.service;

import com.salvation.salvation.communs.exceptions.UserAlreadyHasCharacter;
import com.salvation.salvation.dto.CharacterRequest;
import com.salvation.salvation.dto.UserDto;
import com.salvation.salvation.model.Character;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final CharacterService characterService;

    public UserService(UserRepository userRepository , CharacterService characterService) {
        this.userRepository = userRepository;
        this.characterService = characterService;
    }

    public UserDto createCharacter(CharacterRequest characterRequest) {
        User user = userRepository.findByUsername(characterRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + characterRequest.getUserName()));

        if (user.getCharacter() != null) {
            throw new UserAlreadyHasCharacter("User already has a character.");
        }

       Character character = characterService.createCharacter(characterRequest, user);
        user.setCharacter(character);
        userRepository.save(user);
        return UserDto.fromUser(user);
    }

}
