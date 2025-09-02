package com.sccm.services.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sccm.entities.Contact;
import com.sccm.entities.User;
import com.sccm.helpers.ResourceNotFoundException;
import com.sccm.repositories.ContactRepo;
import com.sccm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();

        contact.setId(contactId);
        
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + id));
    }

    @Override
    public void delete(String id) {
        // contactRepo.deleteById(id);
        var contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + id));

        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public List<Contact> getByUser(User user) {
        // TODO Auto-generated method stub
        return contactRepo.findByUser(user);
    }
}
