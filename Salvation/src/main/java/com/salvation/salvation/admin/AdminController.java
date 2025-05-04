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
    /**
     * Endpoint to get all users.
     *
     * @return ResponseEntity containing a list of UserDto objects.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Endpoint to get a user by ID.
     *
     * @param id the ID of the user to retrieve.
     * @return ResponseEntity containing the UserDto object.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    /**
     * Endpoint to get a user by username.
     *
     * @param username the username of the user to retrieve.
     * @return ResponseEntity containing the UserDto object.
     */
    @GetMapping("/user/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminService.getUserByUsername(username));
    }
    /**
     * Endpoint to ban a user by ID.
     *
     * @param id the ID of the user to ban.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/ban/id/{id}")
    public ResponseEntity<Void> banUserById(@PathVariable Long id) {
        adminService.banUserById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to ban a user by username.
     *
     * @param username the username of the user to ban.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/ban/username/{username}")
    public ResponseEntity<Void> banUserByUsername(@PathVariable String username) {
        adminService.banUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to unban a user by ID.
     *
     * @param id the ID of the user to unban.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/unban/id/{id}")
    public ResponseEntity<Void> unbanUserById(@PathVariable Long id) {
        adminService.unbanUserById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Endpoint to unban a user by username.
     *
     * @param username the username of the user to unban.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/unban/username/{username}")
    public ResponseEntity<Void> unbanUserByUsername(@PathVariable String username) {
        adminService.unbanUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to delete a user by ID.
     *
     * @param id the ID of the user to delete.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Endpoint to delete a user by username.
     *
     * @param username the username of the user to delete.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/delete/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        adminService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to undelete a user by ID.
     *
     * @param id the ID of the user to undelete.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/undelete/id/{id}")
    public ResponseEntity<Void> undeleteUserById(@PathVariable Long id) {
        adminService.unDeleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to undelete a user by username.
     *
     * @param username the username of the user to undelete.
     * @return ResponseEntity with no content.
     */
    @PutMapping("/undelete/username/{username}")
    public ResponseEntity<Void> undeleteUserByUsername(@PathVariable String username) {
        adminService.unDeleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
