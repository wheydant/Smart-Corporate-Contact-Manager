package com.sccm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sccm.entities.User;
import com.sccm.forms.UserForm;
import com.sccm.helpers.MessageType;
import com.sccm.helpers.Messages;
import com.sccm.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(){
        return "redirect:/home";
    }
    
    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request){
        System.out.println("Home page Handler");
        model.addAttribute("name", "Welcome to Smart Corporate Contact Manager");
        model.addAttribute("paragraph", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum voluptatum quae amet optio adipisci voluptate, sint dolores minima consequuntur delectus, nihil sapiente consequatur praesentium saepe animi ducimus, esse architecto repellat a consectetur ipsum? Blanditiis repellendus eaque sint alias obcaecati molestiae magni quam quos voluptatibus assumenda deleniti voluptates, possimus et culpa?");
        model.addAttribute("link", "https://youtu.be/SAqi7zmW1fY?si=8CpQ6drnVwMlMLYl");
        model.addAttribute("currentPath", request.getRequestURI());
        //This is basically name of view.
        return "home";
    }

    //About route

    @RequestMapping("/about")
    public String aboutPage(Model model, HttpServletRequest request) {
        model.addAttribute("isSafe",false);
        System.out.println("About page loading");
        model.addAttribute("currentPath", request.getRequestURI());
        return "about";
    }
    
    //services
    @RequestMapping("/services")
    public String servicePage(Model model, HttpServletRequest request) {
        System.out.println("About page loading");
        model.addAttribute("currentPath", request.getRequestURI());
        return "services";
    }

    @RequestMapping("/contact")
    public String contactPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "contact";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage(Model model) {
        UserForm userForm = new UserForm();
        // We can add default data also
        // userForm.setName("Vedant");
        // userForm.setAbout("This is about");
        model.addAttribute("userForm", userForm);
        return "register";
    }

    //Processing Register
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,HttpSession session){
        System.out.println("Processing Request");
        //Fetch form data
        System.out.println(userForm);

        //Validate data

        if(rBindingResult.hasErrors()){
            return "register";
        }

        //Save Data
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .phoneNumber(userForm.getPhoneNumber())
        // .organization(userForm.getOrganization())
        // .about(userForm.getAbout())
        // .profilePic("src\\main\\resources\\static\\images\\profile.png")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setOrganization(userForm.getOrganization());
        user.setAbout(userForm.getAbout());
        user.setProfilePic("/images/profile.png");
        User savedUser = userService.saveUser(user);

        System.out.println("User Saved !");

        // Message alert using session
        Messages message= Messages.builder().content("Registration Successful !").type(MessageType.green).build();

        session.setAttribute("message", message);
        
        return "redirect:/register";
    }
}
