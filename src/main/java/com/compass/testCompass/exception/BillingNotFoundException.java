package com.compass.testCompass.exception;

public class BillingNotFoundException extends RuntimeException {
    public BillingNotFoundException(String message) {
        super(message);
    }
}
