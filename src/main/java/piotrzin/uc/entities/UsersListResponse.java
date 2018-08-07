package piotrzin.uc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import piotrzin.uc.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UsersListResponse {
    private List<User> users;
}
