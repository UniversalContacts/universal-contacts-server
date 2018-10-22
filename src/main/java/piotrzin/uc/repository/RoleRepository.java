package piotrzin.uc.repository;

import org.springframework.data.repository.CrudRepository;
import piotrzin.uc.model.Role;
import piotrzin.uc.model.RoleName;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);

    boolean existsByName(RoleName roleName);

    List<Role> findAll();
}
