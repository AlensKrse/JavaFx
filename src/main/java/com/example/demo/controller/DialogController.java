package com.example.demo.controller;

import com.example.demo.model.TodoData;
import com.example.demo.model.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField descriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    public TodoItem processNewItem() {
        final String description = descriptionField.getText().trim();
        final String details = detailsArea.getText().trim();
        final LocalDate deadline = deadlinePicker.getValue();

        final TodoItem newTodoItem = new TodoItem(description, details, deadline);
        TodoData.getInstance().addTodoItem(newTodoItem);

        return newTodoItem;
    }
}
