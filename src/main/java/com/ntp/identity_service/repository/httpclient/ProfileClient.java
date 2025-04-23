package com.ntp.identity_service.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ntp.identity_service.configuration.AuthenticationRequestInterceptor;
import com.ntp.identity_service.dto.request.ProfileCreationRequest;
import com.ntp.identity_service.dto.response.UserProfileResponse;

@FeignClient(name = "profile-client", url = "${app.services.profile}", configuration = {
        AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = "application/json")
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest profileCreationRequest);
}
