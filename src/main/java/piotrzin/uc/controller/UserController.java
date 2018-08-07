package piotrzin.uc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import piotrzin.uc.entities.UsersListResponse;
import piotrzin.uc.exception.ResourceNotFoundException;
import piotrzin.uc.model.User;
import piotrzin.uc.repository.UserRepository;
import piotrzin.uc.security.CurrentUser;
import piotrzin.uc.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/isUsernameAvailable")
    public Boolean isUsernameAvailable(@RequestParam(value = "username") String username) {
        return !userRepository.existsByUsername(username);
    }

    @GetMapping("/user/isEmailAvailable")
    public Boolean isEmailAvailable(@RequestParam(value = "email") String email) {
        return !userRepository.existsByEmail(email);
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/username={username}")
    @PreAuthorize("hasRole('USER')")
    public User getUserByUsername(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return user;
    }

    @GetMapping("/user/id={id}")
    @PreAuthorize("hasRole('USER')")
    public User getUserById(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return user;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findAll() {
        List<User> users = userRepository.findAll();
        if (users.size() > 0) {

            logger.info("Found users list with " + users.size() + " users.");

            return new ResponseEntity<>(new UsersListResponse(users), HttpStatus.OK);
        } else {
            logger.error("No users found!");
            return ResponseEntity.noContent().build();
        }
    }
//
//    @GetMapping("/user/{id}")
//    public ResponseEntity<User> findById(@PathVariable(value = "id") Long id) {
//        try {
//            User user = userService.findById(id);
//            logger.info("Found user by id: " + id);
//            return ResponseEntity.ok(user);
//        } catch (ResourceNotFoundException | NoSuchElementException e) {
//            logger.error("No user found with id: " + id);
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/user")
//    public ResponseEntity<User> findByUsername(@RequestParam("username") String username) {
//        User user = userService.findByUsername(username);
//        if (user != null) {
//            logger.info("Found user by username: " + username);
//            return ResponseEntity.ok(user);
//        }
//        logger.error("No user found with username: " + username);
//        return ResponseEntity.notFound().build();
//    }
//
//
//    @PostMapping("user/create")
//    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
//        if (userService.findByUsername(user.getUsername()) != null) {
//            logger.error("User with username: '" + user.getUsername() + "' already exists!");
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//        User newUser = userService.save(user);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(uriComponentsBuilder.path("/user/{id}").buildAndExpand(newUser.getId()).toUri());
//        logger.info("Created user with: " + newUser.toString());
//        return new ResponseEntity<>(headers, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("user/delete/{id}")
//    public ResponseEntity<User> deleteUserById(@PathVariable(value = "id") Long id) {
//        User user = userService.findById(id);
//        if (user == null) {
//            logger.error("Could not delete - user was not found!");
//            return ResponseEntity.notFound().build();
//        } else {
//            userService.delete(id);
//            logger.info("Deleted user with id: " + id);
//            return ResponseEntity.ok(user);
//        }
//    }

    //@PreAuthorize("hasRole('ADMIN')")
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/user/adminTest")
    public String adminTest() {

        return "Admin test passed!";
    }
}
