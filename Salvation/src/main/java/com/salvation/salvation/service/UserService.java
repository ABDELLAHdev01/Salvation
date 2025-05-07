package com.salvation.salvation.service;

import com.salvation.salvation.dto.CharacterRequest;
import com.salvation.salvation.dto.UserDto;
import com.salvation.salvation.enums.Races;
import com.salvation.salvation.enums.Sex;
import com.salvation.salvation.model.Character;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    public UserDto createCharacter(CharacterRequest characterRequest) {
        User user = userRepository.findByUsername(characterRequest.getUserName()).orElseThrow(() -> new RuntimeException("User not found with username: " + characterRequest.getUserName()));
        Character character = Character.builder()
                .name(characterRequest.getName())
                .race(Races.valueOf(characterRequest.getRace()))
                .sex(Sex.valueOf(characterRequest.getSex()))
                .characterNumber(characterRequest.getCharacterNumber())
                .user(user)
                .build();
        user.setCharacter(character);
        return UserDto.fromUser(user);
    }

}
