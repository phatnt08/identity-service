package com.ntp.identity_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import com.nimbusds.jose.JOSEException;
import com.ntp.identity_service.dto.request.AuthenticationRequest;
import com.ntp.identity_service.dto.request.IntrospectRequest;
import com.ntp.identity_service.dto.request.LogoutRequest;
import com.ntp.identity_service.dto.request.RefreshRequest;
import com.ntp.identity_service.dto.response.ApiResponse;
import com.ntp.identity_service.dto.response.AuthenticationResponse;
import com.ntp.identity_service.dto.response.IntrospectResponse;
import com.ntp.identity_service.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController // annotation to create a REST controller
@RequestMapping("/auth") // Map the controller to the /auth endpoint
@RequiredArgsConstructor // Lombok annotation to generate a constructor with all final fields as arguments
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to make the final fields private
public class AuthenticationController {

    // Injecting the AuthenticationService using constructor injection
    AuthenticationService authenticationService;

    /**
     * Endpoint to authenticate a user and generate a token.
     * 
     * @param request the authentication request containing user credentials
     * @return an ApiResponse containing the authentication response with the token
     */
    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    /**
     * Endpoint to introspect a token and retrieve its details.
     * 
     * @param request the introspect request containing the token to be introspected
     * @return an ApiResponse containing the introspect response with token details
     * @throws ParseException if there is an error parsing the token
     * @throws JOSEException if there is an error with the JOSE (JSON Object Signing and Encryption) library
     */
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }
    
    
}
