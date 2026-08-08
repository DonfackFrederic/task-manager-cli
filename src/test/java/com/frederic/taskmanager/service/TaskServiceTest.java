package com.frederic.taskmanager.service;

import com.frederic.taskmanager.exception.TaskNotFoundException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    /* Ajout d'une tache : cas normal */
    @Test
    void shouldAddTask() {
        TaskService taskService = new TaskService();

        Task task = new Task(
                10,
                "Faire les courses",
                TaskStatus.TODO
        );

        taskService.addTask(task);
        assertEquals(1, taskService.getTaskCount());

        String result = taskService.listTasks();
        assertTrue(result.contains("Faire les courses"));
    }

    @Test
    void shouldReplaceTask() {
        TaskService taskService = new TaskService();
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        Task task2 = new Task(10, "Reviser pour les examens", TaskStatus.TODO);
        taskService.addTask(task1);
        taskService.addTask(task2);

        assertEquals(1, taskService.getTaskCount()); // plusieurs ajouts sur un même id provoquent un remplacement
        String result = taskService.listTasks();
        assertFalse(result.contains("Faire les courses"));
    }

    @Test
    void shouldDeleteTask() {
        TaskService taskService = new TaskService();
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        Task task2 = new Task(11, "Reviser pour les examens", TaskStatus.TODO);
        taskService.addTask(task1);
        taskService.addTask(task2);

        taskService.deleteTask(task1.getId());

        assertEquals(1, taskService.getTaskCount());
        String result = taskService.listTasks();
        assertFalse(result.contains("Faire les courses"));
    }

    @Test
    void shouldThrowExceptionWhenDeleteTask() {
        TaskService taskService = new TaskService();
        Task task = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(11));
    }

    @Test
    void shouldMarkAsDone() {
        TaskService taskService = new TaskService();
        Task task = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task);
        taskService.markAsDone(task.getId());

        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenMarkAsDone() {
        TaskService taskService = new TaskService();
        Task task = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task);

        assertThrows(TaskNotFoundException.class, () -> taskService.markAsDone(11));
    }

    @Test
    void shouldListTasks() {
        TaskService taskService = new TaskService();
        Task task1 = new Task(10, "Faire les courses", TaskStatus.TODO);
        taskService.addTask(task1);

        String result = taskService.listTasks();
        // check titles
        assertTrue(result.contains("ID"));
        assertTrue(result.contains("Titre"));
        assertTrue(result.contains("Créée le"));
        assertTrue(result.contains("Statut"));

        assertTrue(result.contains("10"));
        assertTrue(result.contains("Faire les courses"));
        assertTrue(result.contains(TaskStatus.TODO.toString()));
    }

    @Test
    void shouldGetTaskCount() {
        TaskService taskService = new TaskService();
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
}