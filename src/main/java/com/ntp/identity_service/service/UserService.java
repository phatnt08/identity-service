package com.ntp.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ntp.identity_service.dto.request.UserCreationRequest;
import com.ntp.identity_service.dto.request.UserUpdateRequest;
import com.ntp.identity_service.dto.response.UserResponse;
import com.ntp.identity_service.entity.User;
import com.ntp.identity_service.enums.Role;
import com.ntp.identity_service.exception.AppException;
import com.ntp.identity_service.exception.ErrorCode;
import com.ntp.identity_service.mapper.UserMapper;
import com.ntp.identity_service.mapper.UserProfileMapper;
import com.ntp.identity_service.repository.RoleRepository;
import com.ntp.identity_service.repository.UserRepository;
import com.ntp.identity_service.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for handling user-related operations.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class UserService {

    // Injecting the UserRepository using constructor injection
    UserRepository userRepository;

    RoleRepository roleRepository;

    ProfileClient profileClient;

    // Injecting the UserMapper using constructor injection
    UserMapper userMapper;
    UserProfileMapper userProfileMapper;

    // Injecting the PasswordEncoder using constructor injection
    PasswordEncoder passwordEncoder;

    KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Creates a new user.
     * 
     * @param request the user creation request containing user details
     * @return the created user response
     * @throws JsonProcessingException
     */
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        try {
        user = userRepository.save(user);
        var profileRequest = userProfileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());
        var resp = profileClient.createProfile(profileRequest);
        log.info("Profile created successfully: {}", resp);

        } catch (DataIntegrityViolationException e) {
        throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setFirstName(request.getFirstName());
        userResponse.setLastName(request.getLastName());
        userResponse.setDateOfBirth(request.getDateOfBirth());

        // Serialize userResponse to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userResponseJson = "";
        try {
            userResponseJson = objectMapper.writeValueAsString(userResponse);
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // publish message to Kafka topic
        kafkaTemplate.send("test-topic", userResponseJson);

        return userResponse;
    }

    /**
     * Updates an existing user.
     * 
     * @param id      the ID of the user to update
     * @param request the user update request containing updated user details
     * @return the updated user response
     */
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.update(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    /**
     * Deletes a user by ID.
     * 
     * @param id the ID of the user to delete
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    /**
     * Retrieves a list of all users.
     * 
     * @return the list of user responses
     */
    @PreAuthorize("hasRole('ADMIN')") // restrict access to users with the ADMIN role (excute this method before the
                                      // method is called)
    public List<UserResponse> getUsers() {
        log.info("Retrieving all users");
        List<UserResponse> userResponses = userMapper.toUserResponseList(userRepository.findAll());
        return userResponses;
    }

    /**
     * Retrieves a user by ID.
     * 
     * @param id the ID of the user to retrieve
     * @return the user response
     */
    @PostAuthorize("returnObject.username == authentication.name") // restrict access to the user with the same username
    // (excute this method after the method is called, if not authorized, throw an
    // AccessDeniedException)
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        var username = authentication.getName();
        return userMapper.toUserResponse(
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));

    }

}
