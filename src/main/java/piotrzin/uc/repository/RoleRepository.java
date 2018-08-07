package piotrzin.uc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import piotrzin.uc.model.Role;
import piotrzin.uc.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
