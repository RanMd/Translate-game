package com.controllers;

import com.controllers.util.StageFlow;
import com.util.Assets;
import javafx.application.Platform;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.controllers.util.StageFlow.loadStage;


public class MenuViewController implements Initializable {
    private StackPane gameModesStage;
    private HBox optionsView;

    @FXML
    private JFXButton btnConfigure;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnPlay;

    @FXML
    private VBox content;

    @FXML
    private StackPane stackContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPlay.setOnAction(event -> changeToGameModes());
        btnConfigure.setOnAction(event -> changeToOptions());
        btnExit.setOnAction(event -> Platform.exit());
        content.toFront();
        Assets.soundTrack.play();
    }

    public void initController(Stage stage) {
        StageFlow.addStageFlow("Menu", stage);
    }

    private void changeToGameModes() {
        if (gameModesStage == null) {
            gameModesStage = (StackPane) loadStage("GameModesView", this::backFromGameModes);
            stackContainer.getChildren().add(gameModesStage);
        }

        assert gameModesStage != null;
        StageFlow.playSwipe(content, gameModesStage, StageFlow.Direction.LEFT);
    }

    private void changeToOptions() {
        if (optionsView == null) {
            optionsView = (HBox) loadStage("OptionsView", this::backFromOptions);
            stackContainer.getChildren().add(optionsView);
        }

        assert optionsView != null;
        StageFlow.playSwipe(content, optionsView, StageFlow.Direction.RIGHT);
    }

    private void backFromOptions() {
        StageFlow.playSwipe(optionsView, content, StageFlow.Direction.LEFT);
    }

    private void backFromGameModes() {
        StageFlow.playSwipe(gameModesStage, content, StageFlow.Direction.RIGHT);
    }
}