package com.frederic.taskmanager.view;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Gère tout l'affichage dans le terminal (aucune logique métier ici).
 *
 * <p>Toutes les méthodes se contentent d'écrire sur la sortie standard,
 * en respectant les conventions de préfixes : ✔ succès, ✘ erreur,
 * ⚠ avertissement, ℹ information. Le formatage (tableau ASCII, dates)
 * est entièrement isolé ici pour ne jamais polluer {@code TaskService}.</p>
 */
public class ConsoleView {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Affiche la bannière de démarrage (titre + version), commune aux TROIS scénarios de lancement. */
    public void displayWelcomeBanner() {
        System.out.println("========================================");
        System.out.println("  Gestionnaire de tâches CLI — v1.0");
        System.out.println("========================================");
    }

    /**
     * Scénario de démarrage : fichier de sauvegarde trouvé et lisible.
     *
     * @param taskCount le nombre de tâches chargées
     */
    public void displayTasksLoaded(int taskCount) {
        System.out.println("ℹ " + taskCount + " tâche(s) chargée(s) depuis data/tasks.json");
    }

    /** Scénario de démarrage : aucun fichier de sauvegarde trouvé (premier lancement). */
    public void displayNoSaveFound() {
        System.out.println("ℹ Aucune sauvegarde trouvée. Démarrage avec une liste vide.");
        System.out.println("  Un fichier sera créé automatiquement à la première sauvegarde.");
    }

    /** Scénario de démarrage : fichier de sauvegarde présent, mais illisible/corrompu. */
    public void displayCorruptedSave() {
        System.out.println("✘ Le fichier data/tasks.json est illisible ou corrompu.");
        System.out.println("  Démarrage avec une liste vide pour cette session.");
        System.out.println("  Vos anciennes données ne sont pas perdues : corrigez ou renommez");
        System.out.println("  le fichier avant de relancer si vous voulez les récupérer.");
    }

    /** Rappel affiché après un démarrage réussi (chargé ou vide), absent en cas de fichier corrompu. */
    public void displayStartupHint() {
        System.out.println("Tapez 'help' pour la liste des commandes, 'exit' pour quitter.");
        System.out.println();
    }

    /** Affiche l'invite de commande du REPL, sans retour à la ligne. */
    public void displayPrompt() {
        System.out.print("taskcli> ");
    }

    /** Affiche la liste des commandes disponibles. */
    public void displayHelp() {
        System.out.println("Commandes disponibles :");
        System.out.println();
        System.out.println("  add \"<titre>\"     Ajoute une nouvelle tâche");
        System.out.println("  delete <id>       Supprime la tâche portant l'identifiant <id>");
        System.out.println("  done <id>         Marque la tâche <id> comme terminée");
        System.out.println("  list              Affiche toutes les tâches");
        System.out.println("  help              Affiche ce message");
        System.out.println("  exit | quit       Quitte l'application (sauvegarde automatique)");
        System.out.println();
        System.out.println("Exemple : add \"Réviser le chapitre 3\"");
    }

    /**
     * Affiche la liste des tâches sous forme de tableau, ou un message
     * d'information si la liste est vide.
     *
     * @param tasks la liste des tâches à afficher (peut être vide, jamais null)
     */
    public void displayTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            displayInfo("Aucune tâche pour le moment. Utilisez add \"<titre>\" pour en créer une.");
            return;
        }

        String table = AsciiTable.getTable(tasks, Arrays.asList(
                new Column().header("ID").with(t -> String.valueOf(t.getId())),
                new Column().header("Titre").maxWidth(30).with(Task::getTitle),
                new Column().header("Statut").dataAlign(HorizontalAlign.CENTER)
                        .with(t -> t.getStatus().toString()),
                new Column().header("Créée le").with(t -> t.getCreatedAt().format(DATE_FORMATTER))
        ));
        System.out.println(table);

        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        long todo = tasks.size() - done;
        displayInfo(tasks.size() + " tâche(s) au total — " + done + " terminée(s), " + todo + " en cours");
    }

    /**
     * Affiche le succès de l'ajout d'une tâche.
     *
     * @param task la tâche qui vient d'être créée
     */
    public void displayTaskAdded(Task task) {
        displaySuccess("Tâche ajoutée (id=" + task.getId() + ") : \"" + task.getTitle() + "\"");
    }

    /**
     * Affiche le succès de la suppression d'une tâche.
     *
     * @param id l'identifiant de la tâche supprimée
     */
    public void displayTaskDeleted(int id) {
        displaySuccess("Tâche #" + id + " supprimée.");
    }

    /**
     * Affiche le succès du marquage d'une tâche comme terminée.
     *
     * @param id l'identifiant de la tâche marquée terminée
     */
    public void displayTaskDone(int id) {
        displaySuccess("Tâche #" + id + " marquée comme terminée.");
    }

    /**
     * Affiche un avertissement (non bloquant), par ex. une tâche déjà terminée.
     *
     * @param message le message d'avertissement
     */
    public void displayWarning(String message) {
        System.out.println("⚠ " + message);
    }

    /**
     * Affiche un message de succès générique.
     *
     * @param message le message à afficher
     */
    public void displaySuccess(String message) {
        System.out.println("✔ " + message);
    }

    /**
     * Affiche un message d'erreur, sans jamais faire planter le programme
     * ni afficher de stack trace brute.
     *
     * @param message le message d'erreur à afficher
     */
    public void displayError(String message) {
        System.out.println("✘ " + message);
    }

    /**
     * Affiche un message d'information neutre.
     *
     * @param message le message à afficher
     */
    public void displayInfo(String message) {
        System.out.println("ℹ " + message);
    }

    /** Affiche la première étape de la sortie de l'application : sauvegarde en cours. */
    public void displaySavingInProgress() {
        displayInfo("Sauvegarde en cours...");
    }

    /**
     * Affiche le succès de la sauvegarde finale, suivi du message d'adieu.
     *
     * @param taskCount le nombre de tâches sauvegardées
     */
    public void displaySaveSuccessAndGoodbye(int taskCount) {
        displaySuccess(taskCount + " tâche(s) sauvegardée(s) dans data/tasks.json");
        System.out.println("À bientôt !");
    }

    /** Affiche l'échec de la sauvegarde finale et la fermeture de l'application. */
    public void displaySaveFailure() {
        displayError("Échec de la sauvegarde : impossible d'écrire dans data/tasks.json.");
        System.out.println("  Vos modifications de cette session seront perdues.");
        System.out.println("Fermeture de l'application.");
    }
}