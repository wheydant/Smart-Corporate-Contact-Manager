package com.sccm.controllers;

import java.security.Principal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sccm.entities.User;
import com.sccm.forms.UserForm;
import com.sccm.helpers.Helper;
import com.sccm.helpers.MessageType;

import static com.sccm.helpers.Helper.getEmailOfLoggedInUser;
import com.sccm.helpers.Messages;
import com.sccm.services.ContactService;
import com.sccm.services.ImageService;
import com.sccm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    //user dashboard
    @RequestMapping(value = "/dashboard", method=RequestMethod.GET)
    public String userDashboard() {
        return "user/dashboard";
    }
    
    //user profile
    @RequestMapping(value="/profile", method=RequestMethod.GET)
    public String userProfile(Authentication authentication, Model model) {
        return "user/profile";
    }
    
    //Edit user
    @RequestMapping("/settings-view")
    public String settingsView(Model model, Authentication authentication, HttpSession session) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        UserForm userForm = new UserForm();
        userForm.setName(user.getName());
        userForm.setEmail(user.getEmail());
        userForm.setPhoneNumber(user.getPhoneNumber());
        userForm.setAbout(user.getAbout());
        userForm.setOrganization(user.getOrganization());

        model.addAttribute("userForm", userForm);
        model.addAttribute("userId", user.getUserId());

        session.setAttribute("message", Messages.builder().content("Nothing can be edited due to security reasons").type(MessageType.red).build());
        return "user/settings";
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.POST)
    public String updateUser(@PathVariable("userId") String userId,
            @Valid @ModelAttribute UserForm userForm,
            BindingResult bindingResult,
            Model model,
            HttpSession session,
            Authentication authentication) {

        // update the user
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            return "user/settings";
        }

        User user = userService.getUserById(userId).orElse(null);
        logger.info("Hereeeeeeeeeeeeeeeeeeeeeee user {}", user);
        // user.setUserId(userId);
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setOrganization(userForm.getOrganization());

        // process profile image:
        if (userForm.getProfilePic() != null && !userForm.getProfilePic().isEmpty()) {
            logger.info("profile pic is not empty");
            String fileName = UUID.randomUUID().toString();
            String fileUrl = imageService.uploadImage(userForm.getProfilePic(), fileName);
            user.setProfilePic(fileUrl);
            user.setCloudinaryImagePublicId(fileName);
        } else {
            logger.info("profile pic is empty");
        }

        var updatedUser = userService.updateUser(user);
        logger.info("updated user {}", updatedUser);

        session.setAttribute("message", Messages.builder()
            .content("Profile updated successfully!")
            .type(MessageType.green)
            .build());

        return "redirect:/user/profile";
    }

    // @RequestMapping("/change-password-view")
    // public String changePasswordView(Model model) {
    //     ChangePasswordForm changePasswordForm = new ChangePasswordForm();
    //     model.addAttribute("changePasswordForm", changePasswordForm);
    //     return "user/change_password";
    // }

    // @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    // public String changePassword(@Valid @ModelAttribute ChangePasswordForm changePasswordForm,
    //         BindingResult bindingResult,
    //         Model model,
    //         HttpSession session,
    //         Authentication authentication) {

    //     if (bindingResult.hasErrors()) {
    //         return "user/change_password";
    //     }

    //     String username = Helper.getEmailOfLoggedInUser(authentication);
    //     User user = userService.getUserByEmail(username);

    //     // Verify old password
    //     if (!passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword())) {
    //         session.setAttribute("message", Messages.builder()
    //             .content("Current password is incorrect!")
    //             .type(MessageType.red)
    //             .build());
    //         return "user/change_password";
    //     }

    //     // Check if new passwords match
    //     if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
    //         session.setAttribute("message", Messages.builder()
    //             .content("New passwords do not match!")
    //             .type(MessageType.red)
    //             .build());
    //         return "user/change_password";
    //     }

    //     // Update password
    //     user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
    //     userService.updateUser(user);

    //     session.setAttribute("message", Messages.builder()
    //         .content("Password changed successfully!")
    //         .type(MessageType.green)
    //         .build());

    //     return "redirect:/user/profile";
    // }
}
