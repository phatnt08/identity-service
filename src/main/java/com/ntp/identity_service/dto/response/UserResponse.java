package com.ntp.identity_service.dto.response;

import java.time.LocalDate;
import java.util.Set;

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
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    // Set of roles (Set is a collection that contains no duplicate elements)
    Set<String> roles;
}
