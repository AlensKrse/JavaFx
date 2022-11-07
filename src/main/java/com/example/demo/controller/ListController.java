package com.example.demo.controller;

import com.example.demo.model.TodoData;
import com.example.demo.model.TodoItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class ListController {

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {
        todoListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, newItem) -> {
            if (Objects.nonNull(newItem)) {
                final TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
                itemDetailsTextArea.setText(selectedItem.getDetail());
                final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                deadlineLabel.setText(dateTimeFormatter.format(selectedItem.getDeadline()));
            }
        });
        todoListView.setItems(TodoData.getInstance().getTodoItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
        todoListView.setCellFactory(getListViewListCellCallback());
    }

    private Callback<ListView<TodoItem>, ListCell<TodoItem>> getListViewListCellCallback() {
        return todoItemListView -> new ListCell<>() {
            @Override
            protected void updateItem(final TodoItem todoItem, final boolean empty) {
                super.updateItem(todoItem, empty);
                if (empty) {
                    super.setText(null);
                } else {
                    super.setText(todoItem.getDescription());
                    final LocalDate now = LocalDate.now();
                    final LocalDate deadline = todoItem.getDeadline();
                    if (deadline.isBefore(now) || deadline.isEqual(now)) {
                        super.setTextFill(Color.RED);
                    } else if (deadline.equals(now.plusDays(1L))) {
                        super.setTextFill(Color.BROWN);
                    }
                }

            }
        };
    }

    @FXML
    public void showNewItemDialog() {
        final Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use it");

        final URL resource = getClass().getResource("create-item-dialog.fxml");
        final DialogPane dialogPane = dialog.getDialogPane();
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        try {
            dialogPane.setContent(fxmlLoader.load());
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        final ObservableList<ButtonType> buttonTypes = dialogPane.getButtonTypes();
        buttonTypes.add(ButtonType.OK);
        buttonTypes.add(ButtonType.CANCEL);

        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            final DialogController controller = fxmlLoader.getController();
            final TodoItem newTodoItem = controller.processNewItem();
            todoListView.getSelectionModel().select(newTodoItem);
        }
    }

}
