package com.example.demo.controller;

import com.example.demo.model.TodoData;
import com.example.demo.model.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ListController {

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    public void initialize() {
//        TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John", LocalDate.of(2020, Month.DECEMBER, 15));
//        TodoItem item2 = new TodoItem("Doctor appointment", "See doctor Smith at 123 Main Street", LocalDate.of(2020, Month.MAY, 10));
//        TodoItem item3 = new TodoItem("Finish design proposal for client", "I promised Mike I'd email website mockups by Friday 22nd April", LocalDate.of(2020, Month.APRIL, 22));
//        TodoItem item4 = new TodoItem("Pickup Doug at the train station", "Doug's arriving on March 23 on the 5:00", LocalDate.of(2020, Month.MARCH, 23));
//        TodoItem item5 = new TodoItem("Pickup dry cleaning", "The clothes should be ready by Wednesday", LocalDate.of(2020, Month.APRIL, 20));
//
//        todoItems = List.of(item1, item2, item3, item4, item5);
        todoListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, newItem) -> {
            if (Objects.nonNull(newItem)) {
                final TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
                itemDetailsTextArea.setText(selectedItem.getDetail());
                final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                deadlineLabel.setText(dateTimeFormatter.format(selectedItem.getDeadline()));
            }
        });
        todoListView.getItems().addAll(TodoData.getInstance().getTodoItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
    }

}
