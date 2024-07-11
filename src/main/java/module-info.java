module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.jfoenix;
    requires javafx.graphics;
    requires jfxtras.common;
    requires javafx.media;
    requires jfxtras.gauge.linear;
    requires fr.brouillard.oss.cssfx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.bootstrapicons;
    requires java.desktop;

    opens com.controllers to javafx.fxml;
    exports com.controllers;
    exports com.main;
    exports com.models;
}