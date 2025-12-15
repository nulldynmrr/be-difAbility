package com.ippl.difability.exception;

public class JobClosedException extends RuntimeException {
    public JobClosedException() {
        super("Job is not open for applications.");
    }
}
