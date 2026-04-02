package com.blackout.api.polls.infrastructure.web.dto;

public record OptionWithCount(String id, String text, String proposedBy, long voteCount) {}
