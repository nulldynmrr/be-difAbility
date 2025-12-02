package com.ippl.difability.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("You do not have permission to perform this action.");
    }
}
