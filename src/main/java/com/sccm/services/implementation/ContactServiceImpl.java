package com.sccm.services.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String sortDirection) {
        // TODO Auto-generated method stub
        Sort sort = sortDirection.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortField, String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc")? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndNameContaining(user, name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortField, String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc")? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndEmailContaining(user, email, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortField, String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc")? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
    }

}
