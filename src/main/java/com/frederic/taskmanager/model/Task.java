package com.frederic.taskmanager.model;

import java.time.LocalDateTime;

/**
 * Représente une tâche (entité du domaine).
 *
 * TODO (Sprint 1) :
 *  - Ajouter les champs : id, title, status, createdAt
 *  - Générer constructeur(s), getters/setters
 *  - Générer equals/hashCode/toString
 *  - Compléter la JavaDoc
 */
public class Task {
    // TODO Sprint 1
    private int id;
    private String title;
    private TaskStatus status;
    private final LocalDateTime createdAt;

    public Task(int id, String title, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt =  LocalDateTime.now();
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
