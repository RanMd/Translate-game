package com.controllers.components;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public abstract class TextFieldNode {
    private static final UnaryOperator<TextFormatter.Change> filter = change -> {
        String text = change.getText();
        if (text.matches("[a-zA-Z]*")) {
            change.setText(text.toUpperCase());
            return change;
        }
        return null;
    };

    public TextField createTextFieldFormated() {
        TextField textField = new TextField();
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }

    public static void addTextFormatter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void addTextFormatterAll(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.setTextFormatter(new TextFormatter<>(filter));
        }
    }
}
