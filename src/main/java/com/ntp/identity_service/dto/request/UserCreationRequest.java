package com.ntp.identity_service.dto.request;

import java.time.LocalDate;

import com.ntp.identity_service.validator.Annotation.Age.Age;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data // lombok annotation to generate getter and setter
@NoArgsConstructor // lombok annotation to generate no-args constructor
@AllArgsConstructor // lombok annotation to generate all-args constructor
@Builder // lombok annotation to generate builder pattern
@FieldDefaults(level = AccessLevel.PRIVATE) // lombok annotation to set access level of fields
public class UserCreationRequest {

    // @Size annotation to set the size of the field
    @Size(min = 5, message = "USERNAME_INVALID")
    String username;

    // @Size annotation to set the size of the field
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;

    @Age(min = 19, message = "USER_AGE_INVALID") // Custom annotation to validate date of birth
    LocalDate dateOfBirth;
    String city;

}
