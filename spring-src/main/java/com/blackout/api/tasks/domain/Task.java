package com.blackout.api.tasks.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tasks")
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "band_id", nullable = false)
    private String bandId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable = false)
    private String priority = "MEDIUM";

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Task() {}

    public Task(String bandId, String title, String createdBy) {
        this.bandId = bandId;
        this.title = title;
        this.createdBy = createdBy;
    }

    public String getId() { return id; }
    public String getBandId() { return bandId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getAssigneeId() { return assigneeId; }
    public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
    
    public String getCreatedBy() { return createdBy; }
    
    public Instant getDueDate() { return dueDate; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }
    
    public Instant getCreatedAt() { return createdAt; }
}
