package com.sccm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {
    @NotBlank(message="Name is required")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Invalid Email Address")
    private String email;

    @NotBlank(message="Phone number is required")
    @Pattern(regexp="^[0-9]{10}$", message="Invalid Phone Number")
    private String phoneNumber;

    private String address;
    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;

    public boolean getFavorite(){
        return this.favorite;
    }

    //validation
    //size
    //resolution
    private MultipartFile contactImg;
}
