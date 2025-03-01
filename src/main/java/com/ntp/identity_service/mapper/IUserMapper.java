package com.ntp.identity_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ntp.identity_service.dto.request.UserCreationRequest;
import com.ntp.identity_service.dto.request.UserUpdateRequest;
import com.ntp.identity_service.dto.response.UserResponse;
import com.ntp.identity_service.entity.User;

/**
 * Mapper interface for converting between User entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface IUserMapper {
    
    /**
     * Converts a UserCreationRequest to a User entity.
     * 
     * @param request the user creation request
     * @return the User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    /**
     * Converts a User entity to a UserResponse DTO.
     * 
     * @param user the User entity
     * @return the UserResponse DTO
     */
    UserResponse toUserResponse(User user);

    /**
     * Converts a list of User entities to a list of UserResponse DTOs.
     * 
     * @param users the list of User entities
     * @return the list of UserResponse DTOs
     */
    List<UserResponse> toUserResponseList(List<User> users);

    /**
     * Updates an existing User entity with values from a UserUpdateRequest.
     * 
     * @param user the User entity to update
     * @param request the user update request
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void update(@MappingTarget User user, UserUpdateRequest request);

}
