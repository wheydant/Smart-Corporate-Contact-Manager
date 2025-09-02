package com.sccm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sccm.entities.Contact;
import com.sccm.entities.User;
import com.sccm.helpers.Messages;
import com.sccm.forms.ContactForm;
import com.sccm.helpers.Helper;
import com.sccm.helpers.MessageType;
import com.sccm.services.ContactService;
import com.sccm.services.ImageService;
import com.sccm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ImageService imageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired // In protection ready application constructor should be used
    private ContactService contactService;

    @Autowired
    private UserService userService;
    //add contact page: handler

    @RequestMapping("/add")
    public String addContactView(Model model){

        ContactForm contactForm = new ContactForm();
        // contactForm.setName("Test");
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session){

        //Process form data

        //Validate

        if(result.hasErrors()){
            session.setAttribute("message", Messages.builder().content("Please correct the following errors").type(MessageType.red).build());
            return "user/add_contact";
        }
        // get data of root user
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);
        //image processing
        logger.info("file information {}", contactForm.getContactImg().getOriginalFilename());

        String fileName = UUID.randomUUID().toString();

        String fileUrl = imageService.uploadImage(contactForm.getContactImg(), fileName);

        //form --> contact
        Contact contact = new Contact();

        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.getFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());

        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(fileName);
        //save into DB
        //Process form data

        contactService.save(contact);
        System.out.println(contact);
        System.out.println(contactForm);

        //Add a message to save contact
        session.setAttribute("message", Messages.builder().content("You have successfully added new contact").type(MessageType.green).build());

        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContact(Authentication authentication, Model model) {

        //load all the user contact
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        List<Contact> contacts =  contactService.getByUser(user);

        model.addAttribute("contacts", contacts);
        
        return "user/contacts";
    }
    
}
