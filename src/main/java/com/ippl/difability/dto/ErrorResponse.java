// ErrorResponse.java
package com.ippl.difability.dto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")); 
    int status;
    String error; 
    String message;
    String path;
    Map<String, String> errors; 
}