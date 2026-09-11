package com.frederic.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Représente une tâche (entité du domaine).
 */
public class Task {
    // TODO Sprint 1
    private int id;
    private String title;
    private TaskStatus status;
    private final LocalDateTime createdAt;

    public Task(int id, String title, TaskStatus status) {
        this(id, title, status, LocalDateTime.now());
    }

    @JsonCreator
    public Task(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("status") TaskStatus status,
            @JsonProperty("createdAt") LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
