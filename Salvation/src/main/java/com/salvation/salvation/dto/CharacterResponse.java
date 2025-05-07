package com.salvation.salvation.dto;

import com.salvation.salvation.model.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CharacterResponse {
    private Long userName;
    private String name;
    private String race;
    private String sex;
    private int characterNumber;

    public static CharacterResponse fromCharacter(Character character) {
        if (character == null) {
            return new CharacterResponse(null, null, null, null, 0);
        }
        return new CharacterResponse(
                character.getUser().getId(),
                character.getName(),
                character.getRace().name(),
                character.getSex().name(),
                character.getCharacterNumber()
        );
    }
}