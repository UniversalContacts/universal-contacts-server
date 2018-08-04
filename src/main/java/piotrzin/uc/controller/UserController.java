package piotrzin.uc.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import piotrzin.uc.model.User;
import piotrzin.uc.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        try {
            List<User> users = userService.findAll();
            logger.info("Found users list with " + users.size() + " users.");

            for (User user : users) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                System.out.println("User: " + user.getUsername() + " authorities:");
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    System.out.println(authority);
                }
            }

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("No users found!");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> findById(@PathVariable(value = "id") Long id) {
        try {
            User user = userService.findById(id);
            logger.info("Found user by id: " + id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException | NoSuchElementException e) {
            logger.error("No user found with id: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<User> findByUsername(@RequestParam("username") String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            logger.info("Found user by username: " + username);
            return ResponseEntity.ok(user);
        }
        logger.error("No user found with username: " + username);
        return ResponseEntity.notFound().build();
    }


    @PostMapping("user/create")
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        if (userService.findByUsername(user.getUsername()) != null) {
            logger.error("User with username: '" + user.getUsername() + "' already exists!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User newUser = userService.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/user/{id}").buildAndExpand(newUser.getId()).toUri());
        logger.info("Created user with: " + newUser.toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("user/delete/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable(value = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            logger.error("Could not delete - user was not found!");
            return ResponseEntity.notFound().build();
        } else {
            userService.delete(id);
            logger.info("Deleted user with id: " + id);
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/admin")
    public String getTest() {
        return "Test acquired!";
    }
}
