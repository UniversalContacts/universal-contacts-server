package piotrzin.uc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import piotrzin.uc.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
