package com.frederic.taskmanager.controller;

import com.frederic.taskmanager.exception.TaskNotFoundException;
import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import com.frederic.taskmanager.service.TaskService;
import com.frederic.taskmanager.view.ConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    private TaskController taskController;

    @Mock
    private TaskService taskService;
    @Mock
    private ConsoleView consoleView;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(taskService, consoleView);
    }

    @Test
    void shouldReturnTrueWhenNoCommandIsProvided() {

        boolean result = taskController.handleCommand(new String[]{});

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenExitCommandIsProvided() {
        boolean result = taskController.handleCommand(new String[]{"exit"});

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenQuitCommandIsProvided() {
        boolean result = taskController.handleCommand(new String[]{"quit"});

        assertFalse(result);
    }

    @Test
    void shouldDisplayHelpWhenHelpCommandIsProvided() {
        boolean result = taskController.handleCommand(new String[]{"help"});

        verify(consoleView).displayHelp();
    }

    @Test
    void shouldDisplayErrorForUnknownCommand() {
        boolean result = taskController.handleCommand(new String[]{"unknown"});

        verify(consoleView).displayError("Commande inconnue : \"unknown\". Tapez 'help' pour la liste des commandes.");
    }

    @Test
    void shouldReturnTrueForUnknownCommand() {
        boolean result = taskController.handleCommand(
                new String[]{"bonjour"}
        );

        assertTrue(result);
    }

    @Test
    void shouldDisplayErrorWhenAddHasNoTitle() {
        taskController.handleCommand(
                new String[]{"add"}
        );

        verify(consoleView).displayError(
                "Titre manquant. Syntaxe : add \"<titre>\""
        );

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayErrorWhenTitleIsEmpty() {
        taskController.handleCommand(
                new String[]{"add", "\"    \""}
        );

        verify(consoleView).displayError("Le titre ne peut pas être vide.");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayErrorWhenQuoteAreNotClose(){
        taskController.handleCommand(
                new String[]{"add", "\" faire", "du", "café"}
        );

        verify(consoleView).displayError("Guillemets non fermés. Syntaxe : add \"<titre>\"");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayErrorWhenTitleIsTooLong() {
        String title = "a".repeat(201);

        taskController.handleCommand(
                new String[] {"add", title}
        );

        verify(consoleView).displayError("Le titre est trop long (200 caractères maximum).");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayErrorWhenAddFail() throws TaskRepositoryException {
        doThrow(new TaskRepositoryException("Erreur")).when(taskService).addTask(any(Task.class));

        taskController.handleCommand(
                new String[]{"add", "Faire", "les", "courses"}
        );

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskService).addTask(captor.capture());

        Task task = captor.getValue();

        verify(taskService).addTask(task);
        verify(consoleView).displayError("Impossible d'écrire dans data/tasks.json. Tâche non ajoutée.");
    }

    @Test
    void shouldAddTask() throws TaskRepositoryException {

        taskController.handleCommand(
                new String[]{"add", "\"Faire", "les", "courses\""}
        );

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskService).addTask(taskCaptor.capture());

        Task createdTask = taskCaptor.getValue();

        assertEquals(
                "Faire les courses",
                createdTask.getTitle()
        );

        assertEquals(
                TaskStatus.TODO,
                createdTask.getStatus()
        );

        verify(consoleView).displayTaskAdded(createdTask);
    }

    @Test
    void shouldDisplayAnErrorWhenDeleteIdIsMissing(){
        taskController.handleCommand(
                new String[]{"delete"}
        );

        verify(consoleView).displayError("Identifiant manquant. Syntaxe : delete <id>");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayAnErrorWhenDeleteIdIsInvalid(){
        taskController.handleCommand(
                new String[]{"delete", "abc"}
        );

        verify(consoleView).displayError( "\"abc\" n'est pas un identifiant valide (nombre entier attendu).");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayAnErrorWhenDeleteIdIsNotPositive(){
        taskController.handleCommand(
                new String[]{"delete", "0"}
        );

        verify(consoleView).displayError("L'identifiant doit être un entier positif.");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayAnErrorWhenDeleteHasTooManyArguments(){
        taskController.handleCommand(
                new String[]{"delete", "1", "2"}
        );

        verify(consoleView).displayError("Trop d'arguments. Syntaxe : delete <id>");
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldDisplayAnErrorWhenDeleteIdDoesNotExist() throws TaskRepositoryException{
        doThrow(new TaskNotFoundException("Not found"))
                .when(taskService)
                .deleteTask(99);

        taskController.handleCommand(
                new String[]{"delete", "99"}
        );

        verify(taskService).deleteTask(99);
        verify(consoleView).displayError("Aucune tâche trouvée avec l'id 99.");

    }

    @Test
    void shouldDeleteTask() throws TaskRepositoryException {
        taskController.handleCommand(
                new String[]{"delete", "54562"}
        );

        verify(taskService).deleteTask(54562);
        verify(consoleView).displayTaskDeleted(54562);
    }

    @Test
    void shouldDisplayAnErrorWhenDeleteFails() throws TaskRepositoryException {
        doThrow(new TaskRepositoryException("Erreur"))
                .when(taskService)
                .deleteTask(54562);

        taskController.handleCommand(
                new String[]{"delete", "54562"}
        );

        verify(consoleView).displayError("Impossible d'écrire dans data/tasks.json. Suppression non sauvegardée.");
    }

    @Test
    void shouldDisplayList() throws TaskRepositoryException {
        taskController.handleCommand(
                new String[]{"list"}
        );

        verify(taskService).listTasks();
        verify(consoleView).displayTasks(taskService.listTasks());
    }

    @Test
    void shouldMarkTaskAsDone() throws TaskRepositoryException {
        Task task = new Task(1, "faire les courses", TaskStatus.TODO);

        when(taskService.listTasks())
                .thenReturn(List.of(task)
        );

        taskController.handleCommand(
                new String[]{"done", "1"}
        );

        verify(taskService).markAsDone(1);
        verify(consoleView).displayTaskDone(1);
    }

    @Test
    void shouldDisplayWarningWhenMarkTaskAsDone() throws TaskRepositoryException {
        Task task = new Task(1, "faire les courses", TaskStatus.DONE);
        when(taskService.listTasks())
                .thenReturn(List.of(task)
        );

        taskController.handleCommand(
                new String[]{"done", "1"}
        );

        verify(taskService).markAsDone(1);
        verify(consoleView).displayWarning("La tâche #1 est déjà marquée comme terminée.");
    }

    @Test
    void shouldDisplayErrorWhenDoneTaskDoesNotExist() throws TaskRepositoryException {
        when(taskService.listTasks()).thenReturn(List.of());

        doThrow(new TaskNotFoundException("Not found"))
                .when(taskService)
                .markAsDone(99);

        taskController.handleCommand(
                new String[]{"done", "99"}
        );

        verify(taskService).markAsDone(99);
        verify(consoleView).displayError("Aucune tâche trouvée avec l'id 99.");
    }

    @Test
    void shouldDisplayErrorWhenDoneFails() throws TaskRepositoryException {
        doThrow(new TaskRepositoryException("Erreur"))
                .when(taskService)
                .markAsDone(99);

        taskController.handleCommand(
                new String[]{"done", "99"}
        );

        verify(taskService).markAsDone(99);
        verify(consoleView).displayError("Impossible d'écrire dans data/tasks.json. Modification non sauvegardée.");
    }

}