package com.ntp.identity_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data // lombok annotation to generate getter and setter
@NoArgsConstructor // lombok annotation to generate no-args constructor
@AllArgsConstructor // lombok annotation to generate all-args constructor
@Builder // lombok annotation to generate builder pattern
@FieldDefaults(level = AccessLevel.PRIVATE) // lombok annotation to set access level of fields
// @JsonInclude annotation to include properties that are not null
// Not generating if property is null
@JsonInclude(JsonInclude.Include.NON_NULL) // lombok annotation to set access level of fields
public class ApiResponse<T> {
    int code;
    String message;
    T result;
}
