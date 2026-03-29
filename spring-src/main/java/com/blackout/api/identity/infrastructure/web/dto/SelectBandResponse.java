package com.blackout.api.identity.infrastructure.web.dto;

public record SelectBandResponse(String token, UserResponse user, BandInfo band) {}
