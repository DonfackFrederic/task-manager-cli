package com.frederic.taskmanager.repository;

import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

/**
 * Implémentation de {@link TaskRepository} utilisant un fichier JSON comme stockage.
 *
 * Les tâches sont conservées dans une {@link LinkedHashMap} et sérialisées
 * à l'aide de Jackson.
 */
public class JsonTaskRepository implements TaskRepository {
    private final Path filePath;
    private static final Logger logger = LogManager.getLogger(JsonTaskRepository.class);

    private final ObjectMapper objectMapper;

    /**
     * Crée un dépôt utilisant le fichier indiqué pour le stockage des tâches.
     *
     * @param filePath chemin du fichier JSON
     */
    public JsonTaskRepository(Path filePath) {
        this.filePath = filePath;
        objectMapper = JsonMapper.builder().build();
    }


    /**
     * Charge toutes les tâches depuis le fichier JSON.
     *
     * @return tâches chargées, ou une collection vide si le fichier n'existe pas ou est vide
     * @throws TaskRepositoryException si le fichier ne peut pas être lu ou analysé
     */
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

    /**
     * Sauvegarde toutes les tâches dans le fichier JSON.
     *
     * @param tasks tâches à sauvegarder
     * @throws TaskRepositoryException si l'écriture échoue
     */
    @Override
    public void saveAll(LinkedHashMap<Integer, Task> tasks) throws TaskRepositoryException {
        try {
            Files.createDirectories(filePath.getParent());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), tasks);
        } catch (JacksonException | IOException e) {
            logger.error("Échec de l'écriture du fichier {}", filePath, e);
            throw new TaskRepositoryException("Unable to write " + filePath, e);
        }
    }

    /**
     * Vérifie si le fichier de stockage existe.
     *
     * @return {@code true} si le fichier existe, {@code false} sinon
     */
    @Override
    public boolean exists() {
        return Files.exists(filePath);
    }
}