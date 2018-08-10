package piotrzin.uc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import piotrzin.uc.model.Contact;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ContactsListResponse {
    private List<Contact> contacts;
}
