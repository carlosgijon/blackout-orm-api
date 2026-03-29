package com.blackout.api.identity.infrastructure.web.dto;

import java.util.List;

public record LoginResponse(
    String token,
    UserResponse user,
    List<BandInfo> bands
) {}
