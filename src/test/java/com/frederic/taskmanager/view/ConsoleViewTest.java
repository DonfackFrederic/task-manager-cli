package com.frederic.taskmanager.view;

import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleViewTest {

    private ConsoleView consoleView;

    private ByteArrayOutputStream output;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        consoleView = new ConsoleView();

        originalOut = System.out;

        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldDisplayWelcomeBanner() {
        consoleView.displayWelcomeBanner();

        String result = output.toString();

        assertTrue(result.contains("Gestionnaire de tâches CLI — v1.0"));
        assertTrue(result.contains("========================================"));
    }

    @Test
    void shouldDisplayTasksLoaded() {
        consoleView.displayTasksLoaded(5);

        String result = output.toString();

        assertTrue(
                result.contains("ℹ 5 tâche(s) chargée(s) depuis data/tasks.json")
        );
    }

    @Test
    void shouldDisplayNoSaveFound() {

        consoleView.displayNoSaveFound();

        String result = output.toString();

        assertTrue(
                result.contains("ℹ Aucune sauvegarde trouvée. Démarrage avec une liste vide.")
        );
        assertTrue(
                result.contains("Un fichier sera créé automatiquement à la première sauvegarde.")
        );
    }

    @Test
    void shouldDisplayStartupHint() {
        consoleView.displayStartupHint();
        String result = output.toString();

        assertTrue(result.contains("Tapez 'help' pour la liste des commandes, 'exit' pour quitter."));
    }

    @Test
    void shouldDisplayCorruptedSave() {
        consoleView.displayCorruptedSave();
        String result = output.toString();

        assertTrue(
                result.contains("✘ Le fichier data/tasks.json est illisible ou corrompu.")
        );

        assertTrue(
                result.contains("Démarrage avec une liste vide pour cette session.")
        );

        assertTrue(
                result.contains("Vos anciennes données ne sont pas perdues")
        );
    }

    @Test
    void shouldDisplayPrompt() {
        consoleView.displayPrompt();
        String result = output.toString();

        assertEquals("taskcli> ", result);
    }

    @Test
    void shouldDisplaySuccessMessage (){
        consoleView.displaySuccess("Opération réussie");

        assertEquals(
                "✔ Opération réussie" + System.lineSeparator(),
                output.toString()
        );
    }

    @Test
    void shouldDisplayErrorMessage() {
        consoleView.displayError("Une erreur est survenue");

        assertEquals(
                "✘ Une erreur est survenue" + System.lineSeparator(),
                output.toString()
        );
    }

    @Test
    void shouldDisplayWarningMessage() {
        consoleView.displayWarning("La tâche est déjà terminée");

        assertEquals(
                "⚠ La tâche est déjà terminée" + System.lineSeparator(),
                output.toString()
        );
    }

    @Test
    void shouldDisplayTaskDone() {
        consoleView.displayTaskDone(42);

        String result = output.toString();

        assertTrue(result.contains("Tâche #42 marquée comme terminée."));
    }

    @Test
    void shouldDisplayInfoMessage() {
        consoleView.displayInfo("Information importante");

        assertEquals(
                "ℹ Information importante" + System.lineSeparator(),
                output.toString()
        );
    }

    @Test
    void shouldDisplayTaskAdded() {
        Task task = new Task(42, "Apprendre les tests unitaires", TaskStatus.TODO);

        consoleView.displayTaskAdded(task);
        String result = output.toString();

        assertTrue(
                result.contains("✔ Tâche ajoutée (id=42) : \"Apprendre les tests unitaires\"")
        );
    }

    @Test
    void shouldDisplayTaskDeleted() {
        consoleView.displayTaskDeleted(42);
        String result = output.toString();

        assertTrue(result.contains("✔ Tâche #42 supprimée"));
    }

    @Test
    void shouldDisplayMessageWhenThereAreNoTasks() {
        consoleView.displayTasks(List.of());

        String result = output.toString();

        assertTrue(
                result.contains("ℹ Aucune tâche pour le moment.")
        );
        assertFalse(result.contains("ID"));
    }

    @Test
    void shouldDisplayCorrectTaskSummary() {

        List<Task> tasks = List.of(
                new Task(1, "Java", TaskStatus.DONE),
                new Task(2, "JUnit", TaskStatus.DONE),
                new Task(3, "Git", TaskStatus.TODO)
        );

        consoleView.displayTasks(tasks);

        String result = output.toString();

        assertTrue(
                result.contains("3 tâche(s) au total — 2 terminée(s), 1 en cours")
        );
    }

    @Test
    void shouldDisplayHelp() {
        consoleView.displayHelp();
        String result = output.toString();

        assertTrue(result.contains("Commandes disponibles :"));
        assertTrue(result.contains("add \"<titre>\"     Ajoute une nouvelle tâche"));
        assertTrue(result.contains("done <id>         Marque la tâche <id> comme terminée"));
        assertTrue(result.contains("list              Affiche toutes les tâches"));
        assertTrue(result.contains("help              Affiche ce message"));
        assertTrue(result.contains("exit | quit       Quitte l'application (sauvegarde automatique)"));
        assertTrue(result.contains("Exemple : add \"Réviser le chapitre 3\""));
    }

    @Test
    void shouldDisplaySavingInProgress() {
        consoleView.displaySavingInProgress();
        String result = output.toString();

        assertTrue(result.contains("Sauvegarde en cours..."));
    }

    @Test
    void shouldDisplaySaveSuccessAndGoodBye(){
        consoleView.displaySaveSuccessAndGoodbye(10);
        String result = output.toString();

        assertTrue(result.contains("10 tâche(s) sauvegardée(s) dans data/tasks.json"));
    }

    @Test
    void shouldDisplaySaveFailure(){
        consoleView.displaySaveFailure();
        String result = output.toString();

        assertTrue(result.contains("Échec de la sauvegarde : impossible d'écrire dans data/tasks.json."));
        assertTrue(result.contains("Vos modifications de cette session seront perdues."));
        assertTrue(result.contains("Fermeture de l'application."));
    }

}