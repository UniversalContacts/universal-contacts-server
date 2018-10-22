package piotrzin.uc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import piotrzin.uc.entities.RoleRequest;
import piotrzin.uc.exception.ResourceNotFoundException;
import piotrzin.uc.exception.ServerException;
import piotrzin.uc.model.Role;
import piotrzin.uc.model.RoleName;
import piotrzin.uc.model.User;
import piotrzin.uc.repository.RoleRepository;
import piotrzin.uc.repository.UserRepository;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<Set<Role>> getUserRolesByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)).getRoles());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/roles")
    public ResponseEntity<?> grantUserRole(@PathVariable("userId") Long userId,
                                           @RequestBody RoleRequest roleRequest) {
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(roleRequest.getRoleName());

        } catch (IllegalArgumentException e) {
            logger.error("Invalid role name in the request body. See the RoleName enum for all the available role names.");
            throw new ResourceNotFoundException("Role", "roleName", roleRequest.getRoleName());
        }

        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", roleRequest.getRoleName()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.getRoles().add(role);

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<?> discardUserRole(@PathVariable("userId") Long userId,
                                             @PathVariable("roleId") Long roleId) {

        Role mandatoryRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER));

        if (roleId.equals(mandatoryRole.getId())) {
            throw new ServerException("Cannot remove the mandatory role 'ROLE_USER'.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Role toRemove = null;
        for (Role userRole : user.getRoles()) {
            if (userRole.getId().equals(roleId)) {
                toRemove = userRole;
                break;
            }
        }
        if (toRemove == null) {
            throw new ResourceNotFoundException("Role", "id", roleId);
        }
        user.getRoles().remove(toRemove);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}
