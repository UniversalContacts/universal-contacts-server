package piotrzin.uc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.crypto.password.PasswordEncoder;
import piotrzin.uc.exception.ServerException;
import piotrzin.uc.model.Role;
import piotrzin.uc.model.RoleName;
import piotrzin.uc.model.User;
import piotrzin.uc.repository.RoleRepository;
import piotrzin.uc.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        UniversalContactsApplication.class,
        Jsr310JpaConverters.class
})
public class UniversalContactsApplication {

    private static final Logger logger = LoggerFactory.getLogger(UniversalContactsApplication.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        createRoles();
        createAdminAccount();
    }

    private void createRoles() {
        if (roleRepository.existsByName(RoleName.ROLE_USER) && roleRepository.existsByName(RoleName.ROLE_ADMIN)) {
            logger.warn("Base roles have already been created.");
            return;
        }

        roleRepository.save(new Role(RoleName.ROLE_USER));
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        logger.info("Created base roles.");
    }

    private void createAdminAccount() {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ServerException("Administrator role is not present in the database."));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ServerException("User role is not present in the database."));

        if (userRepository.findByRolesContains(adminRole).size() == 0) {
            User admin = new User("Administrator", "admin", "piotrzinbdg@gmail.com", passwordEncoder.encode("password"));
            admin.getRoles().add(userRole);
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
            logger.info("Administrator account created successfully.");
        } else {
            logger.warn("Administrator account already exists.");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(UniversalContactsApplication.class, args);
    }
}
