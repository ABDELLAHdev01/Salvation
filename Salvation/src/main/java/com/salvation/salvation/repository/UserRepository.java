package com.salvation.salvation.repository;

import com.salvation.salvation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndDeletedFalse(String username);
    Optional<User> findByIdAndDeletedFalse(Long id);
    List<User> findAllByDeletedFalse();
    Optional<User> findByUsernameAndDeletedTrue(String username);
    Optional<User> findByIdAndDeletedTrue(Long id);
}
