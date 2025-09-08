package com.sccm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sccm.entities.Contact;
import com.sccm.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{

    //Find contact by user
    //Custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);

    //Custom function if we want to fetch on the basis of User id as user id is not present in the table
    //Custom Query method
    @Query("SELECT c FROM Contact c WHERE c.user.Id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    //Custom Finder method we need to follow same rule findBy then column name then containing, it will automatically triggers like query. Method parameter should also follow column name and keyword
    Page<Contact> findByUserAndNameContaining(User user,String namekeyword, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user,String emailkeyword, Pageable pageable);

    //We cant use findByPhone as our column name is phoneNumber
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumberkeyword, Pageable pageable);


}
