package com.khanhjava.khanh_learn_java.dto.request;

import com.khanhjava.khanh_learn_java.validatior.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_VALID")
    String username;
    @Size(min = 6, message = "PASSWORD_VALID")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 16, message = "DATE_OF_BIRTH_VALID")
    LocalDate dateOfBirth;
}
