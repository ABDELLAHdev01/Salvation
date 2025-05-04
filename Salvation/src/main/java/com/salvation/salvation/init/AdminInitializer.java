package com.salvation.salvation.init;

import com.salvation.salvation.model.Role;
import com.salvation.salvation.model.User;
import com.salvation.salvation.repository.RoleRepository;
import com.salvation.salvation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Value("${admin.username}")
    private String userName;
    @Value("${admin.password}")
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(userName).isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

            User admin = User.builder()
                    .username(userName)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singleton(adminRole))
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();

            userRepository.save(admin);
            logger.info("Admin user created with username: admin");
        }
    }

}
