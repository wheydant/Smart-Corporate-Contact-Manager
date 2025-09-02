package com.sccm.services;

import java.util.List;
import java.util.Optional;

import com.sccm.entities.Contact;
import com.sccm.entities.User;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(String id);

    List<Contact> search(String name, String email, String phoneNumber);

    List<Contact> getByUserId(String userId);

    List<Contact> getByUser(User user);
}
