package com.frederic.taskmanager.repository;

import com.frederic.taskmanager.model.Task;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Implémentation JSON de {@link TaskRepository} (via Gson).
 *
 * TODO (Sprint 2) :
 *  - Lire/écrire dans un fichier JSON (ex: data/tasks.json)
 *  - Gérer le cas "fichier inexistant" (retourner une liste vide)
 *  - Gérer les erreurs de lecture/écriture
 */
public class JsonTaskRepository implements TaskRepository {

    @Override
    public LinkedHashMap<Integer, Task> findAll() {
        throw new UnsupportedOperationException("TODO Sprint 2");
    }

    @Override
    public void saveAll(LinkedHashMap<Integer, Task> tasks) {
        throw new UnsupportedOperationException("TODO Sprint 2");
    }
}
