package com.example.demo.controller;

import com.example.demo.model.TodoData;
import com.example.demo.model.TodoItem;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ListController {

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private CheckBox filterCheckBok;

    private FilteredList<TodoItem> filteredList;
    private static final Predicate<TodoItem> ALL_ITEMS = (item -> true);
    private static final Predicate<TodoItem> TODAY_ITEMS = (item -> item.getDeadline().equals(LocalDate.now()));

    public void initialize() {
        listContextMenu = new ContextMenu();
        final MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            final TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(selectedItem);
        });
        listContextMenu.getItems().addAll(deleteMenuItem);

        todoListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, newItem) -> {
            if (Objects.nonNull(newItem)) {
                final TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
                itemDetailsTextArea.setText(selectedItem.getDetail());
                final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                deadlineLabel.setText(dateTimeFormatter.format(selectedItem.getDeadline()));
            }
        });
        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(), ALL_ITEMS);
        final SortedList<TodoItem> sortedTodoItems = new SortedList<>(filteredList, Comparator.comparing(TodoItem::getDeadline));
        todoListView.setItems(sortedTodoItems);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();
        todoListView.setCellFactory(getListViewListCellCallback());
    }

    private Callback<ListView<TodoItem>, ListCell<TodoItem>> getListViewListCellCallback() {

        return todoItemListView -> {
            final ListCell<TodoItem> listCell = new ListCell<>() {
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

            listCell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (Boolean.TRUE.equals(isNowEmpty)) {
                    listCell.setContextMenu(null);
                } else {
                    listCell.setContextMenu(listContextMenu);
                }
            });

            return listCell;
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

    public void deleteItem(final TodoItem item) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: " + item.getDescription());
        alert.setContentText("Are you sure? Please OK to confirm, or cancel to back out.");

        final Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            TodoData.getInstance().deleteTodoItem(item);
        }
    }

    @FXML
    public void handleKeyPressed(final KeyEvent keyEvent) {
        final TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedItem) && keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
            deleteItem(selectedItem);
        }
    }

    @FXML
    public void handleFilterButton() {
        final MultipleSelectionModel<TodoItem> selectionModel = todoListView.getSelectionModel();
        final TodoItem selectedItem = selectionModel.getSelectedItem();

        if (filterCheckBok.isSelected()) {
            filteredList.setPredicate(TODAY_ITEMS);
            if (filteredList.isEmpty()) {
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                selectionModel.select(selectedItem);
            } else {
                selectionModel.selectFirst();
            }
        } else {
            filteredList.setPredicate(ALL_ITEMS);
            selectionModel.select(selectedItem);
        }
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }
}
