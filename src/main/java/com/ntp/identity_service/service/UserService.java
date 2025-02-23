package com.ntp.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ntp.identity_service.dto.request.UserCreationRequest;
import com.ntp.identity_service.dto.request.UserUpdateRequest;
import com.ntp.identity_service.dto.response.UserResponse;
import com.ntp.identity_service.entity.User;
import com.ntp.identity_service.enums.Role;
import com.ntp.identity_service.exception.AppException;
import com.ntp.identity_service.exception.ErrorCode;
import com.ntp.identity_service.mapper.IUserMapper;
import com.ntp.identity_service.repository.IUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

    IUserRepository userRepository;

    IUserMapper userMapper;

    PasswordEncoder passwordEncoder;
    
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.update(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<UserResponse> getUsers() {
        List<UserResponse> userResponses = userMapper.toUserResponseList(userRepository.findAll());
        return userResponses;
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
    
}
