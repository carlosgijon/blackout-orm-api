package com.blackout.api.polls.infrastructure.web.dto;

import java.util.List;

public record OptionResults(String type, long totalVoters, List<OptionWithCount> options) {}
