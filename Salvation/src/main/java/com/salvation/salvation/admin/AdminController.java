package com.salvation.salvation.admin;

import com.salvation.salvation.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminService.getUserByUsername(username));
    }

    @PutMapping("/ban/id/{id}")
    public ResponseEntity<Void> banUserById(@PathVariable Long id) {
        adminService.banUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/ban/username/{username}")
    public ResponseEntity<Void> banUserByUsername(@PathVariable String username) {
        adminService.banUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/unban/id/{id}")
    public ResponseEntity<Void> unbanUserById(@PathVariable Long id) {
        adminService.unbanUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/unban/username/{username}")
    public ResponseEntity<Void> unbanUserByUsername(@PathVariable String username) {
        adminService.unbanUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/delete/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        adminService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
