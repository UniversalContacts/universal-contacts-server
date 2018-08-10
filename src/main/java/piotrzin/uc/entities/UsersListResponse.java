package piotrzin.uc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import piotrzin.uc.model.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UsersListResponse {
    private List<User> users;
}
