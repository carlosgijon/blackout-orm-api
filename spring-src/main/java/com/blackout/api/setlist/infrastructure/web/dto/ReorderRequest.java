package com.blackout.api.setlist.infrastructure.web.dto;

import java.util.List;

public record ReorderRequest(List<String> ids) {}
