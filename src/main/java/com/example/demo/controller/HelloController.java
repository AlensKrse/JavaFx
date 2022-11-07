package com.example.demo.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class HelloController {
    @FXML
    public TitledPane tp3;
    @FXML
    private Label welcomeText;

    @FXML
    private Button helloButton;

    @FXML
    private Button byeButton;

    @FXML
    private TextField inputField;

    @FXML
    private Label ourLabel;

    @FXML
    public void initialize() {
        helloButton.setDisable(true);
        byeButton.setDisable(true);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to my window!");
    }

    @FXML
    protected void onButtonClick(final ActionEvent event) {
        final String text = inputField.getText();
        final Object source = event.getSource();
        if (source.equals(helloButton)) {
            System.out.println("Hello, " + text);
        } else if (source.equals(byeButton)) {
            System.out.println("Bye, " + text);
        }

        final Runnable task = () -> {
            final String message = Platform.isFxApplicationThread() ? "UI Thread" : "Background";
            System.out.println("On sleep: " + message);
            try {
                Thread.sleep(10000);
                Platform.runLater(() -> {
                    final String message2 = Platform.isFxApplicationThread() ? "UI Thread" : "Background";
                    System.out.println("On change: " + message2);
                    ourLabel.setText("HELL YEA WORKING");
                });
            } catch (final InterruptedException e) {
                throw new UnsupportedOperationException(e);
            }
        };

        new Thread(task).start();
    }

    @FXML
    protected void onKeyReleased() {
        final String text = inputField.getText();
        final boolean empty = text.trim().isEmpty();
        helloButton.setDisable(empty);
        byeButton.setDisable(empty);
    }

}