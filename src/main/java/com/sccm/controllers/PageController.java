package com.sccm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(){
        System.out.println("Home page Handler");
        //This is basically name of view.
        return "home";
    }
}
