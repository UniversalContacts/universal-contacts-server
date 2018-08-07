package piotrzin.uc.entities;

import lombok.Getter;
import lombok.Setter;
import piotrzin.uc.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    @Size(min = 4, max = 48)
    private String name;

    @NotBlank
    @Size(min = 3, max = 16)
    private String username;

    @NotBlank
    @Size(max = 42)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}