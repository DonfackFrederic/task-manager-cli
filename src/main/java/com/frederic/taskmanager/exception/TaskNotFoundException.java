package com.frederic.taskmanager.exception;

/**
 * Levée lorsqu'une opération référence un identifiant de tâche inexistant.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
