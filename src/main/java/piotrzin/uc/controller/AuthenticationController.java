package piotrzin.uc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import piotrzin.uc.entities.JwtAuthenticationResponse;
import piotrzin.uc.entities.LoginRequest;
import piotrzin.uc.entities.RegisterRequest;
import piotrzin.uc.entities.ApiResponse;
import piotrzin.uc.exception.ServerException;
import piotrzin.uc.model.Role;
import piotrzin.uc.model.RoleName;
import piotrzin.uc.model.User;
import piotrzin.uc.repository.RoleRepository;
import piotrzin.uc.repository.UserRepository;
import piotrzin.uc.security.JwtTokenProvider;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken."), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email is already used."), HttpStatus.BAD_REQUEST);
        }

        User user = createUserFromRegisterRequest(registerRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ServerException("User role is not present in the database."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(jwt, jwtTokenProvider.getJwtExpirationTime());

        return ResponseEntity.ok(jwtAuthenticationResponse);
    }

    private User createUserFromRegisterRequest(RegisterRequest registerRequest) {
        return new User(registerRequest.getName(), registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
    }
}
