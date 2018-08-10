package piotrzin.uc.entities;

import lombok.Getter;
import lombok.Setter;
import piotrzin.uc.config.ServerConstants;
import piotrzin.uc.model.Contact;
import piotrzin.uc.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ContactRequest {

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

    protected ContactRequest() {}

    public Contact createContact(User user) {
        Contact contact = new Contact(name, user);

        contact.setEmail(email);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhoneNumber(phoneNumber);
        contact.setDescription(description);

        return contact;
    }
}
