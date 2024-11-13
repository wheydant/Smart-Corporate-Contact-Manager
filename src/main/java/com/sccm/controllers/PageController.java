package com.sccm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home page Handler");
        model.addAttribute("name", "Welcome to Smart Corporate Contact Manager");
        model.addAttribute("paragraph", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum voluptatum quae amet optio adipisci voluptate, sint dolores minima consequuntur delectus, nihil sapiente consequatur praesentium saepe animi ducimus, esse architecto repellat a consectetur ipsum? Blanditiis repellendus eaque sint alias obcaecati molestiae magni quam quos voluptatibus assumenda deleniti voluptates, possimus et culpa?");
        model.addAttribute("link", "https://youtu.be/SAqi7zmW1fY?si=8CpQ6drnVwMlMLYl");
        //This is basically name of view.
        return "home";
    }
}
