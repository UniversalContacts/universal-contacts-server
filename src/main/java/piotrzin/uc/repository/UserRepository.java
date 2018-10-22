package piotrzin.uc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import piotrzin.uc.model.Role;
import piotrzin.uc.model.RoleName;
import piotrzin.uc.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByRolesContains(Role role);

    List<User> findAll();

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
