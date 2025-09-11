package com.sccm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.sccm.entities.User;
import com.sccm.helpers.MessageType;
import com.sccm.helpers.Messages;
import com.sccm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session, Model model) {

        User user = userRepo.findByEmailToken(token).orElse(null);
        Messages message = new Messages();

        if(user != null){
            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);

                message= Messages.builder().content("Email is verified").type(MessageType.green).build();
            }
        }else{
            // Message alert using session
            message= Messages.builder().content("Error in email verification").type(MessageType.red).build();
        }

        session.setAttribute("message", message);
        
        return "redirect:/login";
    }
    
}
