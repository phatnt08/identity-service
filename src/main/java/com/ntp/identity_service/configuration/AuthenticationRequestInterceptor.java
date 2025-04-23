package com.ntp.identity_service.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component // if init a bean for feign client find to init itself to make request and apply for all feign client
public class AuthenticationRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        var bearerToken = servletRequestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Token {}", bearerToken);

        if (StringUtils.hasText(bearerToken)) {
            template.header(HttpHeaders.AUTHORIZATION, bearerToken);
        }
    }

}
