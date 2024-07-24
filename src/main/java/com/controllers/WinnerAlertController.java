package com.controllers;

import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXButton;
import com.models.Category;
import com.models.Player;
import com.models.TranslateGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class WinnerAlertController implements Initializable {
    private Stage myStage;
    private Player playerWinner;

    @FXML
    private JFXButton btnexit;

    @FXML
    private JFXButton btnmenu;

    @FXML
    private JFXButton btnretry;

    @FXML
    private Label labelname;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnmenu.setOnAction(event -> changeToMenu());
        btnexit.setOnAction(event -> Platform.exit());
    }

    public void initController(Stage stage ,Player playerWinner, Runnable onExit) {
        this.myStage = stage;
        this.playerWinner = playerWinner;

        actPlayer();
        btnretry.setOnAction(event -> onExit.run());
    }

    private void changeToMenu() {
        StageFlow.showStage("Menu");
        myStage.close();
    }

    private void actPlayer(){
        labelname.setText(playerWinner.getNombre());
    }


}
