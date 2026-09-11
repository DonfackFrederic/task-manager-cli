package com.frederic.taskmanager.controller;

import com.frederic.taskmanager.exception.TaskNotFoundException;
import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import com.frederic.taskmanager.service.IdGeneratorService;
import com.frederic.taskmanager.service.TaskService;
import com.frederic.taskmanager.view.ConsoleView;

/**

 * Contrôle le traitement des commandes utilisateur et délègue les opérations au service de tâches.
 */
public class TaskController {

    private static final int MAX_TITLE_LENGTH = 200;

    private final TaskService taskService;
    private final ConsoleView consoleView;

    /**

     * Crée un contrôleur avec le service de tâches et la vue console.
     *
     * @param taskService service chargé de gérer les tâches
     * @param consoleView vue utilisée pour afficher les résultats
     */
    public TaskController(TaskService taskService, ConsoleView consoleView) {
        this.taskService = taskService;
        this.consoleView = consoleView;
    }

    /**

     * Traite une commande utilisateur.
     *
     * @param args commande et ses arguments
     * @return {@code false} si la commande demande l'arrêt, {@code true} sinon
     */
    public boolean handleCommand(String[] args) {
        if (args.length == 0) return true;

        String command = args[0].toLowerCase();
        switch (command) {
            case "add": handleAdd(args); break;
            case "delete": handleDelete(args); break;
            case "done": handleDone(args); break;
            case "list": handleList(); break;
            case "help": consoleView.displayHelp(); break;
            case "exit": case "quit": return false;
            default:
                consoleView.displayError(
                        "Commande inconnue : \"" + args[0] + "\". Tapez 'help' pour la liste des commandes.");
        }
        return true;
    }

    /**

     * Valide et ajoute une nouvelle tâche.
     *
     * @param args arguments de la commande {@code add}
     */
    private void handleAdd(String[] args) {
        if (args.length < 2) {
            consoleView.displayError("Titre manquant. Syntaxe : add \"<titre>\"");
            return;
        }
        String title = joinAndUnquote( args);
        if (title == null) return;
        if (title.trim().isEmpty()) {
            consoleView.displayError("Le titre ne peut pas être vide.");
            return;
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            consoleView.displayError("Le titre est trop long (200 caractères maximum).");
            return;
        }

        try {
            Task task = new Task(IdGeneratorService.generateId(), title.trim(), TaskStatus.TODO);
            taskService.addTask(task);
            consoleView.displayTaskAdded(task);
        } catch (TaskRepositoryException e) {
            consoleView.displayError("Impossible d'écrire dans data/tasks.json. Tâche non ajoutée.");
        }
    }

    /**

     * Reconstitue le titre à partir des arguments et retire les guillemets.
     *
     * @param args arguments contenant le titre
     * @return titre reconstitué, ou {@code null} si les guillemets sont invalides
     */
    private String joinAndUnquote(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(' ');
            sb.append(args[i]);
        }
        String raw = sb.toString();
        boolean startsWithQuote = raw.startsWith("\"");
        boolean endsWithQuote = raw.endsWith("\"");

        if (!startsWithQuote) return raw;
        if (raw.length() < 2 || !endsWithQuote) {
            consoleView.displayError("Guillemets non fermés. Syntaxe : add \"<titre>\"");
            return null;
        }
        return raw.substring(1, raw.length() - 1);
    }

    /**

     * Supprime une tâche à partir de son identifiant.
     *
     * @param args arguments de la commande {@code delete}
     */
    private void handleDelete(String[] args) {
        Integer id = parseId(args, "delete");
        if (id == null) return;

        try {
            taskService.deleteTask(id);
            consoleView.displayTaskDeleted(id);
        } catch (TaskNotFoundException e) {
            consoleView.displayError("Aucune tâche trouvée avec l'id " + id + ".");
        } catch (TaskRepositoryException e) {
            consoleView.displayError("Impossible d'écrire dans data/tasks.json. Suppression non sauvegardée.");
        }
    }

    /**

     * Marque une tâche comme terminée.
     *
     * @param args arguments de la commande {@code done}
     */
    private void handleDone(String[] args) {
        Integer id = parseId(args, "done");
        if (id == null) return;

        boolean alreadyDone = taskService.listTasks().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .map(t -> t.getStatus() == TaskStatus.DONE)
                .orElse(false);

        try {
            taskService.markAsDone(id);
            if (alreadyDone) {
                consoleView.displayWarning("La tâche #" + id + " est déjà marquée comme terminée.");
            } else {
                consoleView.displayTaskDone(id);
            }
        } catch (TaskNotFoundException e) {
            consoleView.displayError("Aucune tâche trouvée avec l'id " + id + ".");
        } catch (TaskRepositoryException e) {
            consoleView.displayError("Impossible d'écrire dans data/tasks.json. Modification non sauvegardée.");
        }
    }

    /**

     * Valide et convertit un identifiant fourni en argument.
     *
     * @param args arguments contenant l'identifiant
     * @param commandName nom de la commande associée
     * @return identifiant valide, ou {@code null} si l'argument est invalide
     */
    private Integer parseId(String[] args, String commandName) {
        if (args.length < 2) {
            consoleView.displayError("Identifiant manquant. Syntaxe : " + commandName + " <id>");
            return null;
        }
        if (args.length > 2) {
            consoleView.displayError("Trop d'arguments. Syntaxe : " + commandName + " <id>");
            return null;
        }
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            consoleView.displayError(
                    "\"" + args[1] + "\" n'est pas un identifiant valide (nombre entier attendu).");
            return null;
        }
        if (id <= 0) {
            consoleView.displayError("L'identifiant doit être un entier positif.");
            return null;
        }
        return id;
    }

    private void handleList() {
        consoleView.displayTasks(taskService.listTasks());
    }
}