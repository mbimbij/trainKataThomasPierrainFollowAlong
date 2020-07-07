package com.example.trainkata;

public class UnknownTrainException extends RuntimeException {
    public UnknownTrainException(String message) {
        super(message);
    }
}
