module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires lombok;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.controller;
    exports com.example.demo.model;
    opens com.example.demo.controller to javafx.fxml;
}