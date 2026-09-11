package com.frederic.taskmanager.service;

import com.frederic.taskmanager.exception.TaskNotFoundException;
import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import com.frederic.taskmanager.repository.TaskRepository;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Logique métier de gestion des tâches (ajout, suppression, complétion, listage).
 *
 * <p>Cette classe ne doit contenir aucune logique d'affichage (pas de
 * {@code System.out.println}) ni de dépendance directe à la console
 * ({@code Scanner}), afin de rester testable indépendamment de la CLI.</p>
 *
 * TODO (Sprint 1) :
 *  - addTask(String title)
 *  - deleteTask(int id)
 *  - markAsDone(int id)
 *  - listTasks()
 *
 * TODO (Sprint 2) : injecter un TaskRepository par constructeur.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskService {
    // TODO Sprint 1 / Sprint 2
    private static final Logger logger = LogManager.getLogger(TaskService.class);

    private final LinkedHashMap<Integer, Task> tasks;
    private final TaskRepository jsonTaskRepository;

    public TaskService(TaskRepository jsonTaskRepository) throws TaskRepositoryException {
        this.jsonTaskRepository = jsonTaskRepository;
        tasks = jsonTaskRepository.findAll();

        logger.info("{} tâche(s) chargée(s) en mémoire", tasks.size());
    }

    public TaskService(TaskRepository jsonTaskRepository, LinkedHashMap<Integer, Task> initialTasks) {
        this.jsonTaskRepository = jsonTaskRepository;
        this.tasks = initialTasks;
    }

    public void addTask(Task task) throws TaskRepositoryException {
        tasks.put(task.getId(), task);
        jsonTaskRepository.saveAll(tasks);

        logger.info("Tâche #{} ajoutée - {} tâche(s) en mémoire", task.getId(), tasks.size());
    }

    public void deleteTask(int id) throws TaskRepositoryException {
        Task result = tasks.remove(id);
        if (result == null) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        this.jsonTaskRepository.saveAll(tasks);

        logger.info("Tâche #{} supprimée - {} tâche(s) restante(s)", id, tasks.size());
    }

    public void markAsDone(int id) throws TaskRepositoryException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        task.setStatus(TaskStatus.DONE);
        jsonTaskRepository.saveAll(tasks);

        logger.info("Tâche #{} marquée comme terminée", id);
    }

    public List<Task> listTasks() {
        return new ArrayList<>(tasks.values());
    }

    public int getTaskCount() {
        return tasks.size();
    }
}