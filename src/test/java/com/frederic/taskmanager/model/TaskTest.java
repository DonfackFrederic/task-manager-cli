package com.frederic.taskmanager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;
    @BeforeEach
    void setUp() {
        task = new Task(10, "Faire des exercices de mathématiques", TaskStatus.TODO);
    }

    @Test
    void shouldGetId() {
        assertEquals(10, task.getId());
    }

    @Test
    void setId() {
        task.setId(1200);
        assertEquals(1200, task.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Faire des exercices de mathématiques", task.getTitle());
    }

    @Test
    void setTitle() {
        task.setTitle("Aller à la plage");
        assertEquals("Aller à la plage", task.getTitle());
    }

    @Test
    void getStatus() {
        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    void setStatus() {
        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void getCreatedAt() {
        assertTrue(task.getCreatedAt().isEqual(task.getCreatedAt()));
    }

    @Test
    void testToString() {
        String result = task.toString();
        assertTrue(result.contains("id=10"));
        assertTrue(result.contains("title='Faire des exercices de mathématiques'"));
        assertTrue(result.contains("status=TODO"));
    }
}