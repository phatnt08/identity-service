package com.ntp.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ntp.identity_service.dto.request.ProfileCreationRequest;
import com.ntp.identity_service.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "userId", ignore = true)
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest userProfile);

}
