package com.sccm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sccm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{
    //Extra methods db related operation
    //custom query method
    // custom finder method

    //Function is automatically created by Spring Data GPA
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    //We can also add combination of two attributes
    Optional<User> findByEmailAndPassword(String email, String Password);

    Optional<User> findByEmailToken(String token);
}
