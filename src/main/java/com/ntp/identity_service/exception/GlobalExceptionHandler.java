package com.ntp.identity_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ntp.identity_service.dto.response.ApiResponse;

/**
 * Global exception handler to handle exceptions across the whole application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles general exceptions.
     * 
     * @param e the exception
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        ApiResponse<String> response = new ApiResponse<>();

        response.setCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        response.setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles application-specific exceptions.
     * 
     * @param e the AppException
     * @return a ResponseEntity containing the error response
     */
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException e) {
        ApiResponse<String> response = new ApiResponse<>();

        response.setCode(e.getErrorCode().getCode());
        response.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles validation exceptions for method arguments.
     * 
     * @param e the MethodArgumentNotValidException
     * @return a ResponseEntity containing the error response
     */
    @SuppressWarnings("null")
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse<String> response = new ApiResponse<>();        

        if (e.getFieldError() != null) {
            String errorKey = e.getFieldError().getDefaultMessage();

            ErrorCode errorCode;
            
            try {
                errorCode = ErrorCode.valueOf(errorKey);
            } catch (IllegalArgumentException ex) {
                return handleGeneralException(ex);
            }

            response.setCode(errorCode.getCode());
            response.setMessage(errorCode.getMessage());

        } else {
            return handleGeneralException(e);
        }

        return ResponseEntity.badRequest().body(response);
    }
    
}
