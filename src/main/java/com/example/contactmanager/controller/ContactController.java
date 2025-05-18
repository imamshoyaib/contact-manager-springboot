package com.example.contactmanager.controller;

import com.example.contactmanager.model.Contact;
import com.example.contactmanager.repository.ContactRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactRepository repository;

    public ContactController(ContactRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return repository.findAll().stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .toList();
    }

    @PostMapping
    public Contact addContact(@RequestBody Contact contact) {
        return repository.save(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        return repository.findById(id).map(contact -> {
            contact.setName(updatedContact.getName());
            contact.setPhone(updatedContact.getPhone());
            contact.setEmail(updatedContact.getEmail());
            contact.setAddress(updatedContact.getAddress());
            contact.setGroupType(updatedContact.getGroupType());
            return repository.save(contact);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/search")
    public List<Contact> search(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String phone) {
        if (name != null) return repository.findByNameContainingIgnoreCase(name);
        if (phone != null) return repository.findByPhoneContaining(phone);
        return List.of();
    }

    @GetMapping("/group")
    public List<Contact> group(@RequestParam String type) {
        return repository.findByGroupType(type);
    }
}
