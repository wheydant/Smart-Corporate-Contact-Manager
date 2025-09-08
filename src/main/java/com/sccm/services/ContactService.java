package com.sccm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sccm.entities.Contact;
import com.sccm.entities.User;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(String id);

    Page<Contact> searchByName(String name , int page, int size, String sortField, String sortDirection, User user);

    Page<Contact> searchByEmail(String email, int page, int size, String sortField, String sortDirection, User user);

    Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortField, String sortDirection, User user);

    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);
}
