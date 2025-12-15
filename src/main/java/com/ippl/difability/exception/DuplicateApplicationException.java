package com.ippl.difability.exception;

public class DuplicateApplicationException extends RuntimeException {
    public DuplicateApplicationException() {
        super("You have already applied for this job.");
    }
}
