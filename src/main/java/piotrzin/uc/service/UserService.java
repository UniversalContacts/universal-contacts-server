package piotrzin.uc.service;

import piotrzin.uc.model.User;

import java.util.List;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
    User findByUsername(String username);

    User findById(Long id);
}
