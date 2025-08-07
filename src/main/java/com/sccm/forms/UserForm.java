package com.sccm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @Size(min = 3,message = "Min 3 Characters is required")
    @NotBlank(message = "Username is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message="Invalid Email Address")
    private String email;

    @Size(min = 6, message = "Min 6 characters is required")
    @NotBlank(message = "Password is required")
    private String password;

    @Size(min = 8, max = 12, message = "Invalid Phone Number")
    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;
    @NotBlank(message = "Company Name is required")
    private String organization;
    private String about;

}
