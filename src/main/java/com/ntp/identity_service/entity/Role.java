package com.ntp.identity_service.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Role {

    @Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;
}
