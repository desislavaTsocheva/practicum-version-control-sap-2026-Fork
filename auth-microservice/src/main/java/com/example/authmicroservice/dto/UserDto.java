package com.example.authmicroservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "This field is required")
    @Size(min = 4, max = 20, message = "The username needs to be between 4 and 20 symbols")
    private String username;

    @NotBlank(message = "This field is required")
    private String firstName;

    @NotBlank(message = "This field is required")
    private String lastName;

    @Email(message = "Enter valid email address")
    @NotBlank(message = "This field is required")
    private String email;

    @NotBlank(message = "This field is required")
    @Size(min = 6, message = "The password needs to be at least 6 symbols")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", message = "The password must contain at least one uppercase letter, one lowercase letter, and one number")
    private String password;
}
