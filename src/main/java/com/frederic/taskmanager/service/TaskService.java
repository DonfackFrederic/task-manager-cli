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
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);

    private final LinkedHashMap<Integer, Task> tasks;
    private final TaskRepository jsonTaskRepository;

    /**
     * Initialise le service avec les tâches présentes dans le dépôt.
     *
     * @param jsonTaskRepository dépôt utilisé pour charger et sauvegarder les tâches
     * @throws TaskRepositoryException si le chargement des tâches échoue
     */
    public TaskService(TaskRepository jsonTaskRepository) throws TaskRepositoryException {
        this.jsonTaskRepository = jsonTaskRepository;
        tasks = jsonTaskRepository.findAll();

        logger.info("{} tâche(s) chargée(s) en mémoire", tasks.size());
    }

    /**
     * Initialise le service avec une collection de tâches existante.
     *
     * @param jsonTaskRepository dépôt utilisé pour sauvegarder les tâches
     * @param initialTasks tâches initiales du service
     */
    public TaskService(TaskRepository jsonTaskRepository, LinkedHashMap<Integer, Task> initialTasks) {
        this.jsonTaskRepository = jsonTaskRepository;
        this.tasks = initialTasks;
    }

    /**
     * Ajoute une tâche et sauvegarde la collection mise à jour.
     *
     * @param task tâche à ajouter
     * @throws TaskRepositoryException si la sauvegarde échoue
     */
    public void addTask(Task task) throws TaskRepositoryException {
        tasks.put(task.getId(), task);
        jsonTaskRepository.saveAll(tasks);

        logger.info("Tâche #{} ajoutée - {} tâche(s) en mémoire", task.getId(), tasks.size());
    }

    /**
     * Supprime une tâche et sauvegarde la collection mise à jour.
     *
     * @param id identifiant de la tâche à supprimer
     * @throws TaskNotFoundException si aucune tâche ne correspond à l'identifiant
     * @throws TaskRepositoryException si la sauvegarde échoue
     */
    public void deleteTask(int id) throws TaskRepositoryException {
        Task result = tasks.remove(id);
        if (result == null) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        this.jsonTaskRepository.saveAll(tasks);

        logger.info("Tâche #{} supprimée - {} tâche(s) restante(s)", id, tasks.size());
    }

    /**
     * Marque une tâche comme terminée et sauvegarde la collection mise à jour.
     *
     * @param id identifiant de la tâche à compléter
     * @throws TaskNotFoundException si aucune tâche ne correspond à l'identifiant
     * @throws TaskRepositoryException si la sauvegarde échoue
     */
    public void markAsDone(int id) throws TaskRepositoryException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        task.setStatus(TaskStatus.DONE);
        jsonTaskRepository.saveAll(tasks);

        logger.info("Tâche #{} marquée comme terminée", id);
    }

    /**
     * Retourne les tâches actuellement en mémoire.
     *
     * @return liste des tâches
     */
    public List<Task> listTasks() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Retourne le nombre de tâches actuellement en mémoire.
     *
     * @return nombre de tâches
     */
    public int getTaskCount() {
        return tasks.size();
    }
}