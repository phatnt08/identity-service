package com.ntp.identity_service.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data // lombok annotation to generate getter and setter
@NoArgsConstructor // lombok annotation to generate no-args constructor
@AllArgsConstructor // lombok annotation to generate all-args constructor
@FieldDefaults(level = AccessLevel.PRIVATE) // lombok annotation to set access level of fields
@Builder // lombok annotation to generate builder pattern
@Entity // JPA annotation to make this class an Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Set<String> roles; // Set of roles that the user has (Set # List for uniqueness)

}
