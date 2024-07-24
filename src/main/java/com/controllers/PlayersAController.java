package com.controllers;

import com.controllers.components.PlayerCardNode;
import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXButton;
import com.models.Category;
import com.util.Assets;
import com.util.Data;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class PlayersAController implements Initializable {
    private Stage addPlayerModal;
    private Stage myStage;
    private NamePaneController addPlayerController;
    private Category category;
    private int rounds;

    @FXML
    private JFXButton addPlayerBtn;
    @FXML
    private FlowPane cardPane;
    @FXML
    private JFXButton playButton;
    @FXML
    private Label labelCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        category = Data.getRandomCategory();
        assert category != null;
        labelCategory.setText(category.getName());

        addPlayerBtn.setOnAction(event -> addPlayer());
        playButton.setOnAction(event -> startGame());
        Assets.addSoundToButton(addPlayerBtn, this::addPlayer);
        Assets.addSoundToButton(playButton, this::startGame);

        cardPane.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            playButton.setDisable(cardPane.getChildren().size() < 2);
            addPlayerBtn.setDisable(cardPane.getChildren().size() > 4);
        });

        CSSFX.start();
    }

    public void initController(Stage myStage, int rounds) {
        this.myStage = myStage;
        this.rounds = rounds;

        myStage.setOnCloseRequest(event -> StageFlow.showStage("Menu"));
    }

    private String showModal(String title, String label) {
        if (addPlayerModal == null) initModal();
        else addPlayerController.reset(label, title);
        addPlayerModal.showAndWait();
        return addPlayerController.getData();
    }

    private void initModal() {
        try {
            final FXMLLoader loader = loadFXML("NamePaneView");
            final Parent root = loader.load();

            addPlayerController = loader.getController();

            addPlayerModal = new Stage();
            addPlayerModal.initModality(Modality.APPLICATION_MODAL);
            addPlayerModal.initStyle(StageStyle.UTILITY);
            addPlayerController.initController(addPlayerModal);
            addPlayerModal.setScene(new Scene(root));
            addPlayerModal.setTitle("Crea un nuevo jugador");
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void addPlayer() {
        String newName = showModal("Crear un nuevo jugador", "Escribe el nombre del nuevo jugador:");
        if (newName == null || newName.isEmpty()) return;

        boolean nameExists = false;
        for (Node node : cardPane.getChildren()) {
            if (node instanceof PlayerCardNode) {
                PlayerCardNode playerNode = (PlayerCardNode) node;
                if (playerNode.getPlayerName().equals(newName)) {
                    nameExists = true;
                    break;
                }
            }
        }

        if (nameExists) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Puntajes Finales");
            alert.setHeaderText(null);
            alert.setContentText("El nombre ya está en uso. Por favor, elige otro nombre.");
            alert.showAndWait();
        } else {
            cardPane.getChildren().add(new PlayerCardNode(cardPane, newName, () ->
                    showModal("Cambiar nombre", "Escribe el nuevo nombre del jugador:")));
        }
    }

    private void startGame() {
        final String[] playerNames = new String[cardPane.getChildren().size()];

        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = ((PlayerCardNode) cardPane.getChildren().get(i)).getPlayerName();
        }

        try {
            final FXMLLoader loader = loadFXML("GameAView");
            final Parent root = loader.load();

            final GameAController controller = loader.getController();

            final Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Game");

            controller.initController(stage, playerNames, category, rounds);

            myStage.close();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }


    }
}
