package com.controllers;

import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.util.Assets;
import com.util.interfaces.InitFlowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class ConfigBController implements Initializable, InitFlowController {

    private Runnable onExit;

    @FXML
    private JFXSlider turnsWordSlider;
    @FXML
    private JFXSlider timeSlider;
    @FXML
    private JFXSlider livesSlider;
    @FXML
    private TextField timeField;
    @FXML
    private TextField livesField;
    @FXML
    private TextField turnsWordField;
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXButton returnBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSliderAndField(timeSlider, timeField);
        setupSliderAndField(livesSlider, livesField);
        setupSliderAndField(turnsWordSlider, turnsWordField);

        Assets.addSoundToButton(startButton, this::startGame);
        returnBtn.setOnAction(event -> onExit.run());
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

    public void startGame() {
        try {
            final FXMLLoader loader = loadFXML("PlayersBView");
            final Parent root = loader.load();

            final PlayersBController controller = loader.getController();

            final Stage playersStage = new Stage();
            playersStage.setScene(new Scene(root));
            playersStage.setTitle("Create Players");

            controller.initController(
                    playersStage,
                    Integer.parseInt(livesField.getText()),
                    Integer.parseInt(timeField.getText()),
                    Integer.parseInt(turnsWordField.getText())
            );

            StageFlow.hideStage("Menu");
            playersStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
