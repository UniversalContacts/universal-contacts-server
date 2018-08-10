package piotrzin.uc.repository;

import org.springframework.data.repository.CrudRepository;
import piotrzin.uc.model.Contact;
import piotrzin.uc.model.User;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends CrudRepository<Contact, Long> {
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByName(String name);

    List<Contact> findAll();

    List<Contact> findAllByUserId(Long userId);
    List<Contact> findAllByUserUsername(String username);

    List<Contact> findAllByUser(User user);

    Optional<Contact> findByUserIdAndId(Long userId, Long id);

    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
}
