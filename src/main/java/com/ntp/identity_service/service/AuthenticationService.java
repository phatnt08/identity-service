package com.ntp.identity_service.service;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.esotericsoftware.minlog.Log;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ntp.identity_service.dto.request.AuthenticationRequest;
import com.ntp.identity_service.dto.request.IntrospectRequest;
import com.ntp.identity_service.dto.response.AuthenticationResponse;
import com.ntp.identity_service.dto.response.IntrospectResponse;
import com.ntp.identity_service.entity.User;
import com.ntp.identity_service.exception.AppException;
import com.ntp.identity_service.exception.ErrorCode;
import com.ntp.identity_service.repository.IUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

/**
 * Service class for handling authentication-related operations.
 */
@Service // Indicates that this class is a service component in the Spring application context
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE) // Lombok will make all fields private and assign them the final access level
public class AuthenticationService {

    // Injecting the UserRepository using constructor injection
    IUserRepository userRepository;

    // JWT signer key injected from application properties
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    /**
     * Authenticates a user and generates a JWT token.
     * 
     * @param request the authentication request containing user credentials
     * @return an AuthenticationResponse containing the authentication status and token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Find the user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Verify the password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Generate the JWT token
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(authenticated)
                .token(token)
                .build();
    }

    /**
     * Introspects a JWT token to verify its validity.
     * 
     * @param request the introspect request containing the token to be introspected
     * @return an IntrospectResponse containing the token validity status
     * @throws JOSEException if there is an error with the JOSE (JSON Object Signing and Encryption) library
     * @throws ParseException if there is an error parsing the token
     */
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean valid = signedJWT.verify(verifier);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .valid(valid && expirationTime.after(new Date()))
                .build();
    }

    /**
     * Generates a JWT token for the authenticated user.
     * 
     * @param user the authenticated user
     * @return the generated JWT token
     */
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ntp")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600000))
                .claim("scope", buildScope(user)) // scope claim is standard of oauth2
                .build();

        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            Log.error("cannot create token", e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Builds the scope claim for the JWT token based on the user's roles.
     * 
     * @param user the authenticated user
     * @return the scope claim as a space-separated string
     */
    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(joiner::add);
        }

        return joiner.toString();
    }

}
