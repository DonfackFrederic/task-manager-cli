package com.frederic.taskmanager.repository;

import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

/**
 * Implémentation JSON de {@link TaskRepository} (via Gson).
 *
 * TODO (Sprint 2) :
 *  - Lire/écrire dans un fichier JSON (ex: data/tasks.json)
 *  - Gérer le cas "fichier inexistant" (retourner une liste vide)
 *  - Gérer les erreurs de lecture/écriture
 */
public class JsonTaskRepository implements TaskRepository {
    private final Path filePath;

    private final ObjectMapper objectMapper;

    public JsonTaskRepository(Path filePath) {
        this.filePath = filePath;
        objectMapper = JsonMapper.builder().build();
    }


    @Override
    public LinkedHashMap<Integer, Task> findAll() throws TaskRepositoryException{
        if (!Files.exists(filePath)) {
            return new LinkedHashMap<>();
        }

        try {
            if (Files.size(filePath) == 0) {
                return new LinkedHashMap<>();
            }

            LinkedHashMap<Integer, Task> tasks = objectMapper.readValue(
                    filePath.toFile(),
                    new TypeReference<LinkedHashMap<Integer, Task>>() {}
            );

            return tasks != null
                    ? tasks
                    : new LinkedHashMap<>();

        } catch (JacksonException | IOException e) {
            throw new TaskRepositoryException(
                    "Unable to read " + filePath,
                    e
            );
        }
    }

    @Override
    public void saveAll(LinkedHashMap<Integer, Task> tasks) throws TaskRepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), tasks);
        } catch (JacksonException e) {
            throw new TaskRepositoryException("Unable to write " + filePath, e);
        }
    }

    @Override
    public boolean exists() {
        return Files.exists(filePath);
    }
}
