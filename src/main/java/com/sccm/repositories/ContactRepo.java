package com.sccm.repositories;

import java.util.List;

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
    List<Contact> findByUser(User user);

    //Custom function if we want to fetch on the basis of User id as user id is not present in the table
    //Custom Query method
    @Query("SELECT c FROM Contact c WHERE c.user.Id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);
}
