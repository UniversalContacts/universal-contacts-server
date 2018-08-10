package piotrzin.uc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class RoleRequest {

    @Size(max = 60)
    private String roleName;
}
