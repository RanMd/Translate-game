package com.controllers;

import com.controllers.util.Animations;
import com.jfoenix.controls.JFXButton;
import com.util.Assets;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class NamePaneController implements Initializable {

    private Stage myStage;

    @FXML
    private TextField textData;
    @FXML
    private JFXButton sendInfo;
    @FXML
    private Label infoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[a-zA-Z0-9]*") && change.getControlNewText().length() <= 10){
                change.setText(text.toUpperCase());
                return change;
            }
            return null;
        };

        textData.setTextFormatter(new TextFormatter<>(filter));
        textData.setOnKeyPressed(this::pressEnter);
        sendInfo.setOnAction(event -> sendInfo());
    }

    public void initController(Stage myStage) {
        this.myStage = myStage;
    }

    public String getData() {
        return textData.getText();
    }

    private void pressEnter(KeyEvent event) {
        if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            textData.getStyleClass().remove("error");
        }

        if (event.getCode() == KeyCode.ENTER) {
            sendInfo();
        }
    }

    private void sendInfo() {
        String data = textData.getText();

        if (data.length() < 2) {
            if (!textData.getStyleClass().contains("error")) {
                textData.getStyleClass().add("error");
            }
            Assets.incorrectSound.play();
            Animations.playShakeAnimation(textData);
            return;
        }

        Assets.clickSound.play();
        myStage.close();
    }

    private void reset() {
        textData.clear();
        textData.getStyleClass().remove("error");
    }

    public void reset(String newLabelText, String newTitle) {
        infoLabel.setText(newLabelText);
        myStage.setTitle(newTitle);
        reset();
    }
}
