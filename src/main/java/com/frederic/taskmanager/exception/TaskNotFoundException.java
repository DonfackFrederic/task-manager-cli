package com.frederic.taskmanager.exception;

/**
 * Levée lorsqu'une opération référence un identifiant de tâche inexistant.
 *
 * TODO (Sprint 1) : compléter les constructeurs / la JavaDoc si besoin.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
