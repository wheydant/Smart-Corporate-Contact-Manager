package com.sccm.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sccm.helpers.Helper.getEmailOfLoggedInUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sccm.entities.User;
import com.sccm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication == null) return;
        
        logger.info("Adding logged in user info to the model");
        String userEmail = getEmailOfLoggedInUser(authentication);
        logger.info("User logged in : {}",userEmail);
        
        User user = userService.getUserByEmail(userEmail);

        logger.info("DB Fetched name : {}",user.getName());
        logger.info("DB Fetched email : {}",user.getEmail());

        model.addAttribute("loggedInUser", user);
    }
}
