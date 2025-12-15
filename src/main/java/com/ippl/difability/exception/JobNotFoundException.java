package com.ippl.difability.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException() {
        super("Job not found.");
    }
}
