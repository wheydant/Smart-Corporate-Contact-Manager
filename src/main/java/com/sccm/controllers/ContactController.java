package com.sccm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/contact")
public class ContactController {
    //add contact page: handler

    @RequestMapping("/add")
    public String addContactView(){
        return "user/add_contact";
    }
}
