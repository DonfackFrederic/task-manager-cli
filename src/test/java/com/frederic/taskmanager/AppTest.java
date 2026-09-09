package com.frederic.taskmanager;

import com.frederic.taskmanager.exception.TaskRepositoryException;
import com.frederic.taskmanager.model.Task;
import com.frederic.taskmanager.model.TaskStatus;
import com.frederic.taskmanager.repository.JsonTaskRepository;
import com.frederic.taskmanager.repository.TaskRepository;
import com.frederic.taskmanager.view.ConsoleView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private ByteArrayOutputStream output;
    private PrintStream originalOut;
    private InputStream originalIn;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        originalIn = System.in;
        output = new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void shouldReturnEmptyArrayWhenLineIsEmpty() {
        String[] result = App.tokenize("");

        assertArrayEquals(new String[0], result);
    }

    @Test
    void shouldReturnEmptyArrayWhenLineIsBlank() {
        String[] result = App.tokenize("   ");

        assertArrayEquals(new String[0], result);
    }

    @Test
    void shouldReturnEmptyArrayWhenLineIsNull() {
        String[] result = App.tokenize(null);

        assertArrayEquals(new String[0], result);
    }

    @Test
    void shouldSplitCommandIntoArguments() {
        String[] result = App.tokenize("delete 42");

        assertArrayEquals(new String[]{"delete", "42"}, result);
    }

    @Test
    void shouldIgnoreExtraSpaces() {
        String[] result = App.tokenize("   delete     42   ");

        assertArrayEquals(new String[]{"delete", "42"}, result);
    }

    @Test
    void shouldDisplayNoSaveFoundWhenFileDoesNotExist() throws TaskRepositoryException {

        Path file = tempDir.resolve("tasks.json");
        TaskRepository repository = new JsonTaskRepository(file);
        ConsoleView view = new ConsoleView();
        System.setIn(
                new ByteArrayInputStream("exit\n".getBytes())
        );

        App.run(new String[0], repository, view);
        String result = output.toString();

        assertTrue(result.contains("Aucune sauvegarde trouvée"));
    }

    @Test
    void shouldDisplayTasksLoadedWhenFileExists() throws TaskRepositoryException {
        Path file = tempDir.resolve("tasks.json");
        TaskRepository repository = new JsonTaskRepository(file);

        LinkedHashMap<Integer, Task> tasks = new LinkedHashMap<>();

        Task task = new Task(1, "Apprendre JUnit", TaskStatus.TODO);
        tasks.put(1, task);
        repository.saveAll(tasks);

        ConsoleView view = new ConsoleView();

        System.setIn(
                new ByteArrayInputStream("exit\n".getBytes())
        );

        App.run(new String[0], repository, view);

        String result = output.toString();

        assertTrue(
                result.contains("1 tâche(s) chargée(s)")
        );
    }

    @Test
    void shouldDisplayCorruptedSaveWhenFileIsInvalid() throws IOException, TaskRepositoryException {
        Path file = tempDir.resolve("tasks.json");
        Files.writeString(file, "Ceci n'est pas du JSON");

        TaskRepository repository = new JsonTaskRepository(file);
        ConsoleView view = new ConsoleView();

        System.setIn(
                new ByteArrayInputStream("exit\n".getBytes())
        );
        App.run(new String[0], repository, view);
        String result = output.toString();

        assertTrue(
                result.contains("Le fichier data/tasks.json est illisible ou corrompu.")
        );
    }

    @Test
    void shouldExecuteOneShotCommand() throws TaskRepositoryException {
        Path file = tempDir.resolve("tasks.json");

        TaskRepository repository = new JsonTaskRepository(file);
        ConsoleView view = new ConsoleView();
        App.run(new String[]{"help"}, repository, view);

        String result = output.toString();

        assertTrue(result.contains("Commandes disponibles"));

        assertFalse(result.contains("taskcli>"));
    }

    @Test
    void shouldExitReplWhenExitCommandIsEntered() throws TaskRepositoryException {

        Path file = tempDir.resolve("tasks.json");
        TaskRepository repository = new JsonTaskRepository(file);
        ConsoleView view = new ConsoleView();
        System.setIn(
                new ByteArrayInputStream("exit\n".getBytes())
        );

        App.run(new String[0], repository, view);

        String result = output.toString();

        assertTrue(
                result.contains("À bientôt !")
        );
    }

    @Test
    void shouldExitReplWhenQuitCommandIsEntered() throws TaskRepositoryException {

        Path file = tempDir.resolve("tasks.json");
        TaskRepository repository = new JsonTaskRepository(file);
        ConsoleView view = new ConsoleView();
        System.setIn(
                new ByteArrayInputStream("quit\n".getBytes())
        );

        App.run(new String[0], repository, view);

        String result = output.toString();

        assertTrue(
                result.contains("À bientôt !")
        );
    }

}