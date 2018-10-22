package piotrzin.uc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import piotrzin.uc.entities.ContactRequest;
import piotrzin.uc.exception.ResourceNotFoundException;
import piotrzin.uc.model.Contact;
import piotrzin.uc.repository.ContactRepository;
import piotrzin.uc.service.ContactService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/contacts")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        if (contacts.size() > 0) {
            return ResponseEntity.ok(contacts);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{userId}/contacts")
    public ResponseEntity<List<Contact>> getUserContacts(@PathVariable("userId") Long userId) {
        List<Contact> userContacts = contactRepository.findAllByUserId(userId);

        if (userContacts.size() > 0) {
            return ResponseEntity.ok(userContacts);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/users/{userId}/contacts")
    public ResponseEntity<Contact> createUserContact(@PathVariable("userId") Long userId,
                                                     @Valid @RequestBody ContactRequest contactRequest) {

        Contact result = contactService.createContact(userId, contactRequest);

        contactRepository.save(result);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{contactId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/users/{userId}/contacts/{contactId}")
    public ResponseEntity<Contact> updateUserContact(@PathVariable("userId") Long userId,
                                                     @PathVariable("contactId") Long contactId,
                                                     @Valid @RequestBody ContactRequest contactRequest) {

        Contact result = contactRepository.findByUserIdAndId(userId, contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactId));

        result = contactRequest.createContact(result.getUser());
        result.setId(contactId);

        contactRepository.save(result);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/users/{userId}/contacts/{contactId}")
    public ResponseEntity<?> deleteUserContact(@PathVariable("userId") Long userId,
                                               @PathVariable("contactId") Long contactId) {

        contactRepository.delete(contactRepository.findByUserIdAndId(userId, contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactId)));

        return ResponseEntity.noContent().build();
    }
}
