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
    private TranslateGame game;
    private Stage myStage;
    private Category categoryGame;
    private int maxRounds;
    private final ArrayList<String> names = new ArrayList<>();

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
        game = new TranslateGame();
        btnmenu.setOnAction(event -> changeToMenu());
        btnretry.setOnAction(event -> reset());
        btnexit.setOnAction(event -> Platform.exit());


    }

    public void initController(Stage stage, ArrayList<String> playersName,Player playerWinner, Category category, int maxRounds) {
        this.myStage = stage;
        this.maxRounds = maxRounds;
        this.categoryGame = category;
        this.playerWinner = playerWinner;

        for (String playerName : playersName) {
            Player jugador = new Player(playerName);
            game.addPlayer(jugador);
            names.add(playerName);
        }

        actPlayer();
    }

    private void reset() {
        try {
            final FXMLLoader loader = loadFXML("GameAView");
            final Parent root = loader.load();

            final GameAController controller = loader.getController();

            final Stage gameStage = new Stage();
            gameStage.setScene(new Scene(root));
            gameStage.setTitle("Game");

            controller.initController(gameStage, names, categoryGame, maxRounds);

            myStage.close();
            gameStage.show();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void changeToMenu() {
        StageFlow.showStage("Menu");
        myStage.close();
    }

    private void actPlayer(){
        labelname.setText(playerWinner.getNombre());
    }


}
