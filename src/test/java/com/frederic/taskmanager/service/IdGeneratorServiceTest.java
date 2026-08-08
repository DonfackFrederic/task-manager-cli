package com.frederic.taskmanager.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorServiceTest {
    @Test
    void ShouldGenerateTaskId() {
        String id =""+IdGeneratorService.generateId();
        int idLength = id.length();

        assertEquals(5, idLength);
    }
}