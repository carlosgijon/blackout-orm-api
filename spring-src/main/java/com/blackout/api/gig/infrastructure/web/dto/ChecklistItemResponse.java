package com.blackout.api.gig.infrastructure.web.dto;

import com.blackout.api.gig.domain.ChecklistItem;

public record ChecklistItemResponse(
        String id,
        String checklistId,
        String text,
        boolean done,
        int sortOrder) {

    public static ChecklistItemResponse from(ChecklistItem item) {
        return new ChecklistItemResponse(
                item.getId(), item.getChecklistId(),
                item.getText(), item.isDone(), item.getSortOrder());
    }
}
