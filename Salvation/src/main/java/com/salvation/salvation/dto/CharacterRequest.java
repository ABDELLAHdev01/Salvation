package com.salvation.salvation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CharacterRequest {

    private String userName;
    private String name;
    private String race;
    private String sex;
    private int characterNumber;

}
