package com.controllers;

import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.util.interfaces.InitFlowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class ConfigAController implements Initializable, InitFlowController {

    private Runnable onExit;

    @FXML
    private JFXButton startButton;

    @FXML
    private TextField timeField;

    @FXML
    private JFXSlider timeSlider;

    @FXML
    private JFXButton returnBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSliderAndField(timeSlider, timeField);
        startButton.setOnAction(event -> startGame());
        returnBtn.setOnAction(event -> changeToGameModes());
    }

    @Override
    public void initController(Runnable onExit) {
        this.onExit = onExit;
    }

    private void setupSliderAndField(JFXSlider slider, TextField field) {
        field.setText(String.valueOf((int) slider.getValue()));
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                field.setText(String.valueOf(newValue.intValue())));
    }

    private void changeToGameModes() {
        onExit.run();
    }

    public void startGame() {
        try {
            final FXMLLoader loader = loadFXML("PlayersAView");
            final Parent root = loader.load();

            final PlayersAController controller = loader.getController();

            final Stage playersStage = new Stage();
            playersStage.setScene(new Scene(root));
            playersStage.setTitle("Create Players");

            controller.initController(playersStage, Integer.parseInt(timeField.getText()));

            StageFlow.hideStage("Menu");
            playersStage.show();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
