package com.frederic.taskmanager.service;

import com.frederic.taskmanager.exception.TaskNotFoundException;
import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import com.frederic.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() throws TaskRepositoryException {
        when(taskRepository.findAll())
                .thenReturn(new LinkedHashMap<>());
        this.taskService = new TaskService(taskRepository);
    }


    /* Ajout d'une tache : cas normal */
    @Test
    void shouldAddTask() throws TaskRepositoryException {

        Task task = new Task(10, "Faire les courses", TaskStatus.TODO);

        taskService.addTask(task);
        assertEquals(1, taskService.getTaskCount());

        String result = taskService.listTasks().toString();
        assertTrue(result.contains("Faire les courses"));

        verify(taskRepository).saveAll(any(LinkedHashMap.class));
    }

    @Test
    void shouldReplaceTask() throws TaskRepositoryException {
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        Task task2 = new Task(10, "Reviser pour les examens", TaskStatus.TODO);
        taskService.addTask(task1);
        taskService.addTask(task2);

        assertEquals(1, taskService.getTaskCount()); // plusieurs ajouts sur un même id provoquent un remplacement
        String result = taskService.listTasks().toString();
        assertFalse(result.contains("Faire les courses"));
    }

    @Test
    void shouldDeleteTask() throws TaskRepositoryException {
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        Task task2 = new Task(11, "Reviser pour les examens", TaskStatus.TODO);
        taskService.addTask(task1);
        taskService.addTask(task2);

        taskService.deleteTask(task1.getId());

        assertEquals(1, taskService.getTaskCount());
        String result = taskService.listTasks().toString();
        assertFalse(result.contains("Faire les courses"));
    }

    @Test
    void shouldThrowExceptionWhenDeleteTask() throws TaskRepositoryException {
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(11));

        verify(taskRepository, never()).saveAll(any());
    }

    @Test
    void shouldMarkAsDone() throws TaskRepositoryException {
        Task task = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task);
        taskService.markAsDone(task.getId());

        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenMarkAsDone() throws TaskRepositoryException {
        Task task = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task);

        assertThrows(TaskNotFoundException.class, () -> taskService.markAsDone(11));
    }

    @Test
    void shouldGetTasks() throws TaskRepositoryException {
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task1);

        String result = taskService.listTasks().toString();

        assertTrue(result.contains("10"));
        assertTrue(result.contains("Faire les courses"));
        assertTrue(result.contains(TaskStatus.TODO.toString()));
    }

    @Test
    void shouldGetTaskCount() throws TaskRepositoryException {
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        Task task2 = new Task(11, "Reviser pour les examens", TaskStatus.TODO);
        taskService.addTask(task1);
        taskService.addTask(task2);

        assertEquals(2, taskService.getTaskCount());

        Task task3 = new Task(10, "Jouer au jeux vidéos", TaskStatus.TODO);
        taskService.addTask(task3);

        assertNotEquals(3, taskService.getTaskCount());
        assertEquals(2, taskService.getTaskCount());
    }

    @Test
    void shouldLoadTasksWhenServiceIsCreated()
            throws TaskRepositoryException {

        verify(taskRepository).findAll();
    }

    @Test
    void shouldThrowWhenRepositoryCannotRead() throws TaskRepositoryException {

        TaskRepositoryException exception =
                new TaskRepositoryException(
                        "Impossible de lire le fichier",
                        new IOException("Permission denied")
                );

        when(taskRepository.findAll())
                .thenThrow(exception);

        assertThrows(
                TaskRepositoryException.class,
                () -> new TaskService(taskRepository)
        );
    }

    @Test
    void shouldPropagateRepositoryErrorWhenSaving()
            throws TaskRepositoryException {

        doThrow(
                new TaskRepositoryException(
                        "Impossible d'écrire le fichier",
                        new IOException("Permission denied")
                )
        ).when(taskRepository).saveAll(any());

        Task task = new Task(
                10,
                "Faire les courses",
                TaskStatus.TODO
        );

        assertThrows(
                TaskRepositoryException.class,
                () -> taskService.addTask(task)
        );
    }
}