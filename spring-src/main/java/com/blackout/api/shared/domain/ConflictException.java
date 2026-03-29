package com.blackout.api.shared.domain;

public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super(message);
    }
}
