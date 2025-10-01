package com.khanhjava.khanh_learn_java.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_VALID")
    private String username;
    @Size(min = 6, message = "PASSWORD_VALID")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
