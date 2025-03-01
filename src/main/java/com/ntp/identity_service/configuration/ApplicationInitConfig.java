package com.ntp.identity_service.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ntp.identity_service.entity.User;
import com.ntp.identity_service.enums.Role;
import com.ntp.identity_service.repository.IUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class to initialize the application with default settings.
 */
@Configuration // spring annotation to indicate that this class is a configuration class
@RequiredArgsConstructor // lombok annotation to generate a constructor with all final fields as arguments
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE) // lombok annotation to make the final fields private
@Slf4j // lombok annotation to inject a logger
public class ApplicationInitConfig {

    // Injecting the PasswordEncoder using constructor injection
    PasswordEncoder passwordEncoder;

    /**
     * Bean to run custom logic after the application context is loaded.
     * 
     * @param userRepository the user repository to interact with the user data
     * @return an ApplicationRunner to execute the custom logic
     */
    @Bean
    ApplicationRunner applicationRunner(IUserRepository userRepository) {
        return _ -> {
            System.out.println("Application started");

            // Check if the admin user exists, if not, create it
            if (userRepository.findByUsername("admin").isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userRepository.save(admin);
                log.info("Admin user created");
            }
        };
    }
}
