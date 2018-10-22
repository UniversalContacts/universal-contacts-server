package piotrzin.uc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import piotrzin.uc.config.ServerConstants;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 48)
    private String name;

    @Size(max = 42)
    @Email
    private String email;

    @Size(max = 32)
    private String firstName;

    @Size(max = 32)
    private String lastName;

    @Size(max = ServerConstants.MAX_CONTACT_DESCRIPTION_LENGTH)
    private String description;

    @Size(min = 7, max = ServerConstants.MAX_PHONE_NUMBER_LENGTH)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Contact(String name, User user)
    {
        this.name = name;
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Contact{id=%d, name=%s, email=%s, user=%s}", id, name, email, user.toString());
    }
}
