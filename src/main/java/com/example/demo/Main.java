package com.example.demo;

import com.example.demo.model.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader root = new FXMLLoader(Main.class.getResource("todolist/main-window.fxml"));
        Scene scene = new Scene(root.load(), 900, 500);
        stage.setTitle("Todo List");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        try {
            TodoData.getInstance().storeTodoItems();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}