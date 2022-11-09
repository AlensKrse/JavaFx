package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class SampleController {

    public static final String WEB_SITE = "https://www.ss.com";

    @FXML
    private Label label;

    @FXML
    private Button button4;

    @FXML
    private GridPane gridPane;

    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        button4.setEffect(new DropShadow());
    }

    @FXML
    public void handleMouseEnter() {
        label.setScaleX(2.0);
        label.setScaleY(2.0);
    }

    @FXML
    public void handleMouseExit() {
        label.setScaleX(1);
        label.setScaleY(1);
    }

    @FXML
    public void handleClick() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save application file");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"));
        final File file = chooser.showSaveDialog(gridPane.getScene().getWindow());

        try (final BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("test text");
            writer.newLine();
            writer.write("123");
        } catch (final IOException e) {
            throw new UnsupportedOperationException(e);
        }

        final String path = Optional.of(file).map(File::getAbsolutePath).orElse(null);
        System.out.println(path);
    }

    @FXML
    public void handleLinkClick() {
//        try {
//            Desktop.getDesktop().browse(new URI(WEB_SITE));
//        } catch (final IOException | URISyntaxException e) {
//            throw new UnsupportedOperationException(e);
//        }
       final WebEngine webEngine = webView.getEngine();
       webEngine.load(WEB_SITE);
    }

}
