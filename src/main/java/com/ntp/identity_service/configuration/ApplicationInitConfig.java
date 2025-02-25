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

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(IUserRepository userRepository) {
        return _ -> {
            System.out.println("Application started");

            if (userRepository.findByUsername("admin").isEmpty()) {
                HashSet<String> roles = new HashSet<String>();
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
