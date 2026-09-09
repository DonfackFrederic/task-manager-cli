package com.frederic.taskmanager.repository;

import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonTaskRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReturnEmptyMapWhenFileDoesNotExist() throws TaskRepositoryException {
        Path file = tempDir.resolve("task.json");

        JsonTaskRepository repository = new JsonTaskRepository(file);
        LinkedHashMap <Integer, Task> result = repository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenFileEmpty() throws IOException, TaskRepositoryException {
        Path file = tempDir.resolve("task.json");
        Files.createFile(file);

        JsonTaskRepository repository = new JsonTaskRepository(file);
        LinkedHashMap <Integer, Task> result = repository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveTask() throws IOException, TaskRepositoryException {
        Path path = tempDir.resolve("task.json");

        JsonTaskRepository repository = new JsonTaskRepository(path);
        LinkedHashMap <Integer, Task> tasks = new LinkedHashMap<>();

        Task task = new Task(1, "Faire des exercices de Mathématiques", TaskStatus.TODO);
        tasks.put(task.getId(), task);

        repository.saveAll(tasks);
        assertTrue(Files.exists(path));
        assertTrue(Files.size(path) > 0);
    }

    @Test
    void shouldSaveAndReadTasks() throws TaskRepositoryException {

        Path file = tempDir.resolve("task.json");

        JsonTaskRepository repository =
                new JsonTaskRepository(file);

        LinkedHashMap<Integer, Task> tasks =
                new LinkedHashMap<>();

        Task task1 = new Task(
                1,
                "Faire des exercices de Mathématiques",
                TaskStatus.TODO
        );

        Task task2 = new Task(
                2,
                "Faire les courses",
                TaskStatus.DONE
        );

        tasks.put(task1.getId(), task1);
        tasks.put(task2.getId(), task2);

        repository.saveAll(tasks);

        LinkedHashMap<Integer, Task> result =
                repository.findAll();

        assertEquals(2, result.size());

        Task loadedTask = result.get(1);

        assertNotNull(loadedTask);

        assertEquals(task1.getId(), loadedTask.getId());
        assertEquals(task1.getTitle(), loadedTask.getTitle());
        assertEquals(task1.getStatus(), loadedTask.getStatus());
        assertEquals(task1.getCreatedAt(), loadedTask.getCreatedAt());
    }

    @Test
    void shouldThrowExceptionWhenJsonIsInvalid() throws IOException {
        Path file = tempDir.resolve("task.json");
        Files.writeString(file, "Ceci n'est pas un JSON");

        JsonTaskRepository repository = new JsonTaskRepository(file);

        assertThrows(TaskRepositoryException.class, repository::findAll);
    }

    @Test
    void shouldReturnFalseWhenFileDoesNotExist() {

        Path file = tempDir.resolve("task.json");

        JsonTaskRepository repository =
                new JsonTaskRepository(file);

        boolean result = repository.exists();

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenFileExists() throws IOException {

        Path file = tempDir.resolve("task.json");

        Files.createFile(file);

        JsonTaskRepository repository =
                new JsonTaskRepository(file);

        boolean result = repository.exists();

        assertTrue(result);
    }
}