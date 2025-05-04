package com.salvation.salvation.auth;

import com.salvation.salvation.DTO.LoginRespone;
import com.salvation.salvation.DTO.RegisterRequest;
import com.salvation.salvation.model.User;
import com.salvation.salvation.model.Role;
import com.salvation.salvation.repository.RoleRepository;
import com.salvation.salvation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }


        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("USER")
                                .build()
                ));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(Set.of(userRole));

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespone> login(@RequestBody RegisterRequest request) {
        // Implement login logic here

        // For now, just return a dummy response
        LoginRespone loginRespone = new LoginRespone();
        loginRespone.setUsername(request.getUsername());
        return ResponseEntity.ok(loginRespone);
    }
}
