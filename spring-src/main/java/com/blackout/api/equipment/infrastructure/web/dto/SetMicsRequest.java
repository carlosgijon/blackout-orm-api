package com.blackout.api.equipment.infrastructure.web.dto;

import java.util.List;

public record SetMicsRequest(List<String> micIds) {
}
