package com.ippl.difability.exception;

public class ApplicationReviewedException extends RuntimeException {
    public ApplicationReviewedException() {
        super("Application already reviewed.");
    }
} 

