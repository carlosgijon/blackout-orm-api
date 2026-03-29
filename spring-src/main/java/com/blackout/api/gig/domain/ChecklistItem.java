package com.blackout.api.gig.domain;

import jakarta.persistence.*;

@Entity @Table(name = "checklist_items")
public class ChecklistItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(name = "checklist_id", nullable = false) private String checklistId;
    @Column(nullable = false) private String category = "otro";
    @Column(nullable = false) private String text;
    @Column(nullable = false) private boolean done = false;
    @Column(name = "sort_order", nullable = false) private int sortOrder = 0;

    protected ChecklistItem() {}
    public ChecklistItem(String checklistId, String text) {
        this.checklistId = checklistId; this.text = text;
    }

    public String getId() { return id; }
    public String getChecklistId() { return checklistId; }
    public String getCategory() { return category; }
    public void setCategory(String c) { category = c; }
    public String getText() { return text; }
    public void setText(String t) { text = t; }
    public boolean isDone() { return done; }
    public void setDone(boolean d) { done = d; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int s) { sortOrder = s; }
}
