package app.rbac.rbac.Controller;

import app.rbac.rbac.Entity.*;
import app.rbac.rbac.Helper.Exception.User.*;
import app.rbac.rbac.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Tag(name = "Users")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EDITOR')")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "Adding New User Endpoint",
            summary = "Adding new user to the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "User Created Successfully",
                            responseCode = "201"
                    )
            }
    )
    @PreAuthorize("hasAuthority('ADD')")
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(
            @RequestParam String phone,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam List<String> roleNames,
            @RequestParam List<String> privilegeNames
    ) {
        try {
            User newUser = userService.addUser(phone, email, password, roleNames, privilegeNames);
            return ResponseEntity.ok(newUser);
        } catch (EmailExistException | PhoneExistException e) {
            // Handle the specific exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            description = "Listing all users Endpoint",
            summary = "List all users in the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "All users in the system has been listed",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAuthority('WATCH')")
    @GetMapping("/list")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
            description = "Listing all roles Endpoint",
            summary = "List all roles in the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "All roles in the system has been listed",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAuthority('WATCH')")
    @GetMapping("/listRole")
    public ResponseEntity<List<Role>> listAllRoles() {
        List<Role> roles = userService.listAllRoles();
        return ResponseEntity.ok(roles);
    }

    @Operation(
            description = "Listing all privileges Endpoint",
            summary = "List all privileges in the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "All privileges in the system has been listed",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAuthority('WATCH')")
    @GetMapping("/listPrivilege")
    public ResponseEntity<List<Privilege>> listAllPrivileges() {
        List<Privilege> privileges = userService.listAllPrivileges();
        return ResponseEntity.ok(privileges);
    }

    @Operation(
            description = "Get user with phone Endpoint",
            summary = "Get user with phone in the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "Getting user by phone number",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAuthority('WATCH')")
    @GetMapping("/{phone}")
    public ResponseEntity<User> findUserByPhone(@PathVariable("phone") String phone) {
        User user = userService.findUserByPhone(phone);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            description = "Get user with email Endpoint",
            summary = "Get user with email in the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "Getting user by email",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAuthority('WATCH')")
    @GetMapping("/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            description = "Updating user Endpoint",
            summary = "Updating user information in the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "User Updated Successfully",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{currentPhone}")
    public ResponseEntity<User> updateUser(
            @PathVariable String currentPhone,
            @RequestParam(required = false) String newPhone,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) List<String> roleNames,
            @RequestParam(required = false) List<String> privilegeNames
    ) {
        try {
            User updatedUser = userService.updateUser(
                    currentPhone,
                    newPhone,
                    newEmail,
                    roleNames,
                    privilegeNames
            );
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException | EmailExistException | PhoneExistException e) {
            // Handle the specific exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(
            description = "Updating password Endpoint",
            summary = "Updating password for the user",
            responses = {
                    @ApiResponse(
                            description = "Password Updated Successfully",
                            responseCode = "200"
                    )
            }
    )
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable("id") long id, @RequestBody String newPassword) {
        try {
            userService.updatePassword(id, newPassword);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Operation(
            description = "Deleting user Endpoint",
            summary = "Delete user from the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "User has been deleted",
                            responseCode = "204"
                    )
            }
    )
    @PreAuthorize("hasAuthority('REMOVE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Operation(
            description = "Deleting user softly Endpoint",
            summary = "Delete user softly from the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "User has been deleted softly",
                            responseCode = "204"
                    )
            }
    )
    @PreAuthorize("hasAuthority('REMOVE')")
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteUser(@PathVariable long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.ok("User deleted softly.");
    }
}
