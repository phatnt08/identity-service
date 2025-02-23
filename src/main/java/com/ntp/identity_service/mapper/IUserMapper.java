package com.ntp.identity_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.ntp.identity_service.dto.request.UserCreationRequest;
import com.ntp.identity_service.dto.request.UserUpdateRequest;
import com.ntp.identity_service.dto.response.UserResponse;
import com.ntp.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void update(@MappingTarget User user, UserUpdateRequest request);

}
