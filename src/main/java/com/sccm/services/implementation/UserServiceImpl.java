package com.sccm.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sccm.entities.User;
import com.sccm.helpers.AppConstants;
import com.sccm.helpers.ResourceNotFoundException;
import com.sccm.repositories.UserRepo;
import com.sccm.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        //user id needs to be generate unique and randomly
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        //Testing
        user.setEnabled(true);
        //Password encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //Role list
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info(user.toString());
        //Default profile pic
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        //Update user2
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setContacts(user.getContacts());
        user2.setOrganization(user.getOrganization());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        User save = userRepo.save(user2);

        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        userRepo.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepo.findById(userId).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
