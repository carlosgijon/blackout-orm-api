package com.blackout.api.tasks.infrastructure.web.dto;

import com.blackout.api.tasks.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class TaskDto {

    public record CreateTaskRequest(
        @NotBlank String title,
        String description,
        String priority,
        String assigneeId,
        Instant dueDate
    ) {}

    public record UpdateTaskRequest(
        String title,
        String description,
        TaskStatus status,
        String priority,
        String assigneeId,
        Instant dueDate
    ) {}

    public record TaskResponse(
        String id,
        String bandId,
        String title,
        String description,
        TaskStatus status,
        String priority,
        String assigneeId,
        String createdBy,
        Instant dueDate,
        Instant createdAt
    ) {}
}
