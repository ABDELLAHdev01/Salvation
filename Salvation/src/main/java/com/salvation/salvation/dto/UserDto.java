package com.salvation.salvation.dto;

import com.salvation.salvation.model.Role;
import com.salvation.salvation.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private String username;
    private Set<Role> roles;
    private CharacterResponse character;

    public static UserDto fromUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        return new UserDto(user.getUsername(), user.getRoles(),
                user.getCharacter() != null ? CharacterResponse.fromCharacter(user.getCharacter()) : null);
    }
}