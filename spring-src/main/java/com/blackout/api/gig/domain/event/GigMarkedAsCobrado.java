package com.blackout.api.gig.domain.event;

public record GigMarkedAsCobrado(String gigId, String bandId, String title, String pay) {}
