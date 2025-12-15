package com.ippl.difability.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.ippl.difability.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 400 Bad Request
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class,
        IncompleteRequestException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequests(Exception exception, HttpServletRequest request){
        Map<String, String> errors = null;
        String message = "Invalid URL or Request Body.";

        // DTO
        if (exception instanceof MethodArgumentNotValidException validationException){
            errors = validationException.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage,
                    (existing, replacement) -> existing, 
                    LinkedHashMap::new
                ));
            message = "Input validation failed.";
        }
        // JSON
        else if (exception instanceof HttpMessageNotReadableException notReadableException){
            if (notReadableException.getCause() instanceof InvalidFormatException ife) {
                message = String.format("Invalid value '%s'. Expected a valid constant for type %s.", 
                    ife.getValue(), ife.getTargetType().getSimpleName());
            } else {
                message = "Bad JSON request.";
            }
        }
        
        // API URL
        else if (exception instanceof MethodArgumentTypeMismatchException mismatchException) {
            String requiredType = mismatchException.getRequiredType() != null 
                ? mismatchException.getRequiredType().getSimpleName() 
                : "Value";
            message = String.format("Type mismatch for parameter '%s'. Received '%s', expected %s.",
                mismatchException.getName(), mismatchException.getValue(), requiredType);
        }

        // Update profile
        else if (exception instanceof IncompleteRequestException incomplete) {
            message = incomplete.getMessage();
        }

        return buildError(HttpStatus.BAD_REQUEST, message, request, errors);
    } 

    // 401 Unauthorized
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            InvalidCredentialsException exception, HttpServletRequest request){
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage(), request, null);
    }

    // 403 Forbidden
    @ExceptionHandler({
        ForbiddenException.class,
        AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleForbidden(
            Exception exception, HttpServletRequest request){
        String message = exception instanceof AccessDeniedException 
            ? "Access denied"
            : exception.getMessage();
        return buildError(HttpStatus.FORBIDDEN, message, request, null);
    }

    // 404 Not Found
    @ExceptionHandler({
        UserNotFoundException.class,
        JobNotFoundException.class,
        ApplicationNotFoundException.class,
        ResourceNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(
            RuntimeException exception, HttpServletRequest request){
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request, null);
    }

    // 409 Conflict
    @ExceptionHandler({
        EmailAlreadyExistsException.class,
        DuplicateApplicationException.class,
        JobClosedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(
            RuntimeException exception, HttpServletRequest request){
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), request, null);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerError(
            Exception exception, HttpServletRequest request) {
        log.error("Unexpected Server Error at {}", request.getRequestURI(), exception);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected server error occurred.",
            request,
            null
        );
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status, String message, HttpServletRequest request, Map<String, String> errors) {
        ErrorResponse response = ErrorResponse.builder()
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message)
            .path(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .errors(errors)
            .build();
        return new ResponseEntity<>(response, status);
    }
}