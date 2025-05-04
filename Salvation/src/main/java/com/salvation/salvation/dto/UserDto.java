package com.salvation.salvation.dto;

import com.salvation.salvation.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private String username;


    public static UserDto fromUser(User user) {
        return new UserDto(user.getUsername());
    }
}
