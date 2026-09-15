package com.frederic.taskmanager.service;

import java.nio.file.Path;

/**
 * Fournit les chemins utilisés par l'application selon le système d'exploitation.
 *
 * <p>Les répertoires sont déterminés à partir des conventions propres à
 * Linux, macOS et Windows afin de garantir un stockage portable des données
 * et des journaux de l'application.</p>
 */
public final class AppPathsService {

    private static final String OS =
            System.getProperty("os.name").toLowerCase();

    /**
     * Retourne le répertoire dans lequel les données de l'application sont stockées.
     *
     * @return le répertoire de stockage des données de TaskCLI
     */
    public static Path dataDirectory() {
        if (OS.contains("win")) {
            return Path.of(
                    System.getenv("LOCALAPPDATA"),
                    "taskcli"
            );
        }

        if (OS.contains("mac")) {
            return Path.of(
                    System.getProperty("user.home"),
                    "Library",
                    "Application Support",
                    "taskcli"
            );
        }

        return Path.of(
                System.getProperty("user.home"),
                ".local",
                "share",
                "taskcli"
        );
    }

    /**
     * Retourne le répertoire dans lequel les journaux de l'application sont stockés.
     *
     * @return le répertoire de stockage des journaux de TaskCLI
     */
    public static Path logDirectory() {
        if (OS.contains("win")) {
            return Path.of(
                    System.getenv("LOCALAPPDATA"),
                    "taskcli",
                    "logs"
            );
        }

        if (OS.contains("mac")) {
            return Path.of(
                    System.getProperty("user.home"),
                    "Library",
                    "Logs",
                    "taskcli"
            );
        }

        return Path.of(
                System.getProperty("user.home"),
                ".local",
                "state",
                "taskcli"
        );
    }
}