package com.ippl.difability.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ippl.difability.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException exception, HttpServletRequest request){

        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                FieldError::getField, 
                FieldError::getDefaultMessage, 
                (existing, replacement) -> existing, 
                LinkedHashMap::new 
			)
        );

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Input validation failed for fields.")
            .path(request.getRequestURI())
            .errors(errors)
            .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(exception.getMessage())
            .path(request.getRequestURI())
            .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    // 401
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(InvalidCredentialsException exception, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
            .message(exception.getMessage())
            .path(request.getRequestURI())                
            .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    // 403
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException exception, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .message(exception.getMessage())
            .path(request.getRequestURI())
            .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    // 409
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ResourceConflictException exception, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .error(HttpStatus.CONFLICT.getReasonPhrase())
            .message(exception.getMessage())
            .path(request.getRequestURI())
            .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleServerError(RuntimeException exception, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("An unexpected server error occurred.") 
            .path(request.getRequestURI())
            .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}