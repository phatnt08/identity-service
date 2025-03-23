package com.ntp.identity_service.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.ntp.identity_service.dto.request.IntrospectRequest;
import com.ntp.identity_service.service.AuthenticationService;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signer-key}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder jwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());
            if (!response.isValid()) {
                throw new JwtException("Token invalid");
            }
        } catch (JOSEException | java.text.ParseException e) {
            throw new JwtException(e.getMessage());
        }

        SecretKeySpec secretKey = new SecretKeySpec(signerKey.getBytes(), "HS512");
        
        jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();

        return jwtDecoder.decode(token);
    }
}
