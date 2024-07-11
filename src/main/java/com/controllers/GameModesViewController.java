package com.controllers;

import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXToggleNode;
import com.util.interfaces.InitFlowController;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.controllers.util.StageFlow.loadStage;

public class GameModesViewController implements Initializable, InitFlowController {
    private Runnable returnToMenu;
    private VBox gameModeA;
    private VBox gameModeB;

    @FXML
    private StackPane stackContainer;

    @FXML
    private VBox content;

    @FXML
    private JFXButton btnPlay;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXToggleNode btnGameA;

    @FXML
    private JFXToggleNode btnGameB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnReturn.setOnAction(event -> changeToMenu());

        btnGameA.setOnAction(event -> btnGameB.setSelected(false));

        btnGameB.setOnAction(event -> btnGameA.setSelected(false));

        btnPlay.disableProperty().bind(btnGameA.selectedProperty().not().and(btnGameB.selectedProperty().not()));

        btnPlay.setOnAction(event -> {
            if (btnGameA.isSelected()) changeToConfigA();
            else changeToConfigB();
        });
    }

    @Override
    public void initController(Runnable returnToMenu) {
        this.returnToMenu = returnToMenu;
    }

    private void changeToMenu() {
        returnToMenu.run();
    }

    private void changeToConfigA() {
        if (gameModeA == null) {
            gameModeA = (VBox) loadStage("ConfigAView", this::backFromConfigA);
            stackContainer.getChildren().add(gameModeA);
        }
        StageFlow.playSwipe(content, gameModeA, StageFlow.Direction.UP_LEFT);
    }

    private void backFromConfigA() {
        StageFlow.playSwipe(gameModeA, content, StageFlow.Direction.DOWN_RIGHT);
    }

    private void changeToConfigB() {
        if (gameModeB == null) {
            gameModeB = (VBox) loadStage("ConfigBView", this::backFromConfigB);
            stackContainer.getChildren().add(gameModeB);
        }
        StageFlow.playSwipe(content, gameModeB, StageFlow.Direction.DOWN_LEFT);
    }

    private void backFromConfigB() {
        StageFlow.playSwipe(gameModeB, content, StageFlow.Direction.UP_RIGHT);
    }
}
