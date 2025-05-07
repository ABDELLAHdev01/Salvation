package com.salvation.salvation.repository;


import com.salvation.salvation.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CharacterRepository extends JpaRepository<Character, Long> {
}
