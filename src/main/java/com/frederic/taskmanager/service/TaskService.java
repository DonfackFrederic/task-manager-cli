package com.frederic.taskmanager.service;

import com.frederic.taskmanager.exception.TaskNotFoundException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
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
public class TaskService {
    // TODO Sprint 1 / Sprint 2
    private final LinkedHashMap<Integer, Task> tasks = new LinkedHashMap<>();
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        Task result = tasks.remove(id);
        if (result == null) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
    }

    public void markAsDone(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        task.setStatus(TaskStatus.DONE);
    }

    public String listTasks() {
        return AsciiTable.getTable(tasks.values(), Arrays.asList(
                new Column().header("ID").with(task -> String.valueOf(task.getId())),
                new Column().header("Titre").maxWidth(30).with(Task::getTitle),
                new Column().header("Statut").dataAlign(HorizontalAlign.CENTER).with(t -> t.getStatus().toString()),
                new Column().header("Créée le").with(task -> task.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        ));
    }

    public int getTaskCount() {
        return tasks.size();
    }
}
