package com.frederic.taskmanager.exception;

/**
 * Levée lorsqu'une opération dans le TaskRepository échoue
 *
 */
public class TaskRepositoryException extends Throwable {

    public TaskRepositoryException(String message) {
        super(message);
    }

    public TaskRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
