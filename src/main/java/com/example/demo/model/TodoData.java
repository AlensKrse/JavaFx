package com.example.demo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TodoData {

    private static final TodoData INSTANCE = new TodoData();
    private static final String FILE_NAME = "todo_list_items.txt";

    private ObservableList<TodoItem> todoItems;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static TodoData getInstance() {
        return INSTANCE;
    }

    private TodoData() {
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(final ObservableList<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public void addTodoItem(final TodoItem newTodoItem) {
        todoItems.add(newTodoItem);
    }

    public void loadTodoItems() {
        todoItems = FXCollections.observableArrayList();
        final Path path = Paths.get(FILE_NAME);
        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            String input;
            while (Objects.nonNull(input = reader.readLine())) {
                final String[] itemPieces = input.split("\t");
                final String description = itemPieces[0];
                final String details = itemPieces[1];
                final String dateString = itemPieces[2];

                final LocalDate date = LocalDate.parse(dateString, FORMATTER);
                final TodoItem todoItem = new TodoItem(description, details, date);
                this.todoItems.add(todoItem);
            }
        } catch (final IOException e) {
            final String message = String.format("Failed to read file with todo items, message: '%s'", e.getLocalizedMessage());
            throw new UnsupportedOperationException(message);
        }
    }

    public void storeTodoItems() {
        final Path path = Paths.get(FILE_NAME);
        try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (final TodoItem item : this.todoItems) {
                final String line = String.format("%s\t%s\t%s", item.getDescription(), item.getDetail(), item.getDeadline().format(FORMATTER));
                writer.write(line);
                writer.newLine();
            }
        } catch (final IOException e) {
            final String message = String.format("Failed to write todo items to file, message: '%s'", e.getLocalizedMessage());
            throw new UnsupportedOperationException(message);
        }

    }

    public void deleteTodoItem(final TodoItem item) {
        todoItems.remove(item);
    }
}
