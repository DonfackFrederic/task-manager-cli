package com.frederic.taskmanager;

import com.frederic.taskmanager.controller.TaskController;
import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.repository.JsonTaskRepository;
import com.frederic.taskmanager.repository.TaskRepository;
import com.frederic.taskmanager.service.TaskService;
import com.frederic.taskmanager.view.ConsoleView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Point d'entrée de l'application Gestionnaire de tâches CLI.
 *
 * Chaque exécution traite une seule commande passée en argument
 * (ex: java -jar app.jar add "Titre") puis se termine.
 */
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    private static final Path DATA_FILE = Path.of("data", "tasks.json");

    /**
     * Instancie les couches (repository, vue)
     * et délègue le traitement.
     *
     * @param args arguments de la ligne de commande : la commande suivie
     *             de ses paramètres (ex: {"add", "Réviser le chapitre 3"})
     */
    public static void main(String[] args) throws TaskRepositoryException {
        logger.info("Démarrage de l'application");

        TaskRepository repository = new JsonTaskRepository(DATA_FILE);
        ConsoleView view = new ConsoleView();

        run(args, repository, view);
    }

    static void run(
            String[] args,
            TaskRepository repository,
            ConsoleView view
    ) throws TaskRepositoryException {
        boolean fileExisted = repository.exists();

        TaskService taskService;
        boolean corrupted = false;
        try {
            taskService = new TaskService(repository);
        } catch (TaskRepositoryException e) {
            corrupted = true;
            logger.warn("Le fichier de sauvegarde n'a pas pu être chargé. Démarrage avec une liste vide.", e);
            taskService = new TaskService(repository, new LinkedHashMap<>());
        }

        if (args.length > 0) {
            if (corrupted) {
                view.displayCorruptedSave();
                System.out.println();
            }
            runOneShot(args, taskService, view);
        } else {
            runRepl(taskService, view, fileExisted, corrupted);
        }
    }

    private static void runOneShot(String[] args, TaskService taskService, ConsoleView view) {
        TaskController controller = new TaskController(taskService, view);
        controller.handleCommand(args);
        // Pas de save explicite ici : chaque mutation dans TaskService
        // sauvegarde déjà elle-même (voir addTask/deleteTask/markAsDone).
    }

    private static void runRepl(TaskService taskService, ConsoleView view,
                                boolean fileExisted, boolean corrupted) {
        view.displayWelcomeBanner();
        if (corrupted) {
            view.displayCorruptedSave();
            System.out.println();
        } else if (fileExisted) {
            view.displayTasksLoaded(taskService.getTaskCount());
            view.displayStartupHint();
        } else {
            view.displayNoSaveFound();
            view.displayStartupHint();
        }

        TaskController controller = new TaskController(taskService, view);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            view.displayPrompt();
            if (!scanner.hasNextLine()) break;
            String[] tokens = tokenize(scanner.nextLine());
            running = controller.handleCommand(tokens);
        }

        // sauvegarde finale = filet de sécurité (chaque commande a déjà
        // sauvegardé individuellement) ; permet aussi le message d'adieu du doc.
        view.displaySavingInProgress();
        view.displaySaveSuccessAndGoodbye(taskService.getTaskCount());

        logger.info("Arrêt de l'application - {} tâche(s) en mémoire", taskService.getTaskCount());
    }

    static String[] tokenize(String line) {
        if (line == null || line.isBlank()) return new String[0];
        return line.trim().split("\\s+");
    }
}