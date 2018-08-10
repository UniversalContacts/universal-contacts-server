package piotrzin.uc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import piotrzin.uc.entities.ContactRequest;
import piotrzin.uc.exception.ResourceNotFoundException;
import piotrzin.uc.model.Contact;
import piotrzin.uc.model.User;
import piotrzin.uc.repository.ContactRepository;
import piotrzin.uc.repository.UserRepository;

@Service
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact createContact(Long userId, ContactRequest contactRequest) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Contact result = contactRequest.createContact(owner);

        return contactRepository.save(result);
    }
}
