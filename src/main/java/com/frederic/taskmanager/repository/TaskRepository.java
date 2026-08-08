package com.frederic.taskmanager.repository;

import com.frederic.taskmanager.model.Task;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Abstraction de la persistance des tâches.
 *
 * <p>Permet de changer le mode de stockage (JSON, texte, base de données...)
 * sans impacter {@code TaskService}.</p>
 *
 * TODO (Sprint 2) : implémenter dans JsonTaskRepository.
 */
public interface TaskRepository {

    LinkedHashMap<Integer, Task> findAll();

    void saveAll(LinkedHashMap<Integer, Task> tasks);
}
