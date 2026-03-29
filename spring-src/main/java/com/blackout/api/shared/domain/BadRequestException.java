package com.blackout.api.shared.domain;

public class BadRequestException extends DomainException {
    public BadRequestException(String message) {
        super(message);
    }
}
