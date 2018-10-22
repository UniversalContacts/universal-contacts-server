package piotrzin.uc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import piotrzin.uc.exception.ResourceNotFoundException;
import piotrzin.uc.model.User;
import piotrzin.uc.repository.UserRepository;
import piotrzin.uc.security.CurrentUser;
import piotrzin.uc.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/is-username-available/{username}")
    public Boolean isUsernameAvailable(@PathVariable(name = "username") String username) {
        return !userRepository.existsByUsername(username);
    }

    @GetMapping("/is-email-available/{email}")
    public Boolean isEmailAvailable(@PathVariable(name = "email") String email) {
        return !userRepository.existsByEmail(email);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.size() > 0) {
            logger.info("Found users list with " + users.size() + " users.");
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            logger.error("No users found!");
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return ResponseEntity.ok(user);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}
