package com.example.authmicroservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Потребителското име е задължително поле")
    @Size(min = 4, max = 20, message = "Потребителското име трябва да е между 4 и 20 символа")
    private String username;

    @NotBlank(message = "Името е задължително поле")
    private String firstName;

    @NotBlank(message = "Фамилията е задължително поле")
    private String lastName;

    @Email(message = "Въведете валиден имейл адрес")
    @NotBlank(message = "Имейлът е задължително поле")
    private String email;

    @Size(min = 8, message = "Паролата трябва да е поне 8 символа")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
            message = "Паролата трябва да съдържа поне една цифра, една малка и една главна буква")
    private String password;
}
