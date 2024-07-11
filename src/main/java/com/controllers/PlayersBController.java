package com.controllers;

import com.controllers.components.PlayerCardNode;
import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class PlayersBController implements Initializable {
    private final Map<String, Integer> gameValues = new HashMap<>(3);
    private Stage addPlayerModal;
    private Stage myStage;
    private NamePaneController addPlayerController;

    @FXML
    private JFXButton addPlayerBtn;
    @FXML
    private FlowPane cardPane;
    @FXML
    private JFXButton playButton;

    @FXML
    private JFXComboBox<String> comboSelectCat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addPlayerBtn.setOnAction(event -> addPlayer());
        playButton.setOnAction(event -> startGame());
        Assets.addSoundToButton(addPlayerBtn, this::addPlayer);
        Assets.addSoundToButton(playButton, this::startGame);

        comboSelectCat.valueProperty().addListener((observable, oldValue, newValue) ->
                playButton.setDisable(!(cardPane.getChildren().size() > 1)));

        cardPane.getChildren().addListener((ListChangeListener.Change<? extends javafx.scene.Node> c) -> {
            playButton.setDisable(!(cardPane.getChildren().size() > 1 && comboSelectCat.getValue() != null));
            addPlayerBtn.setDisable(cardPane.getChildren().size() > 4);
        });

        loadDataToCombo();

        CSSFX.start();
    }

    public void initController(Stage myStage, int lives, int time, int wordTurns) {
        this.myStage = myStage;
        gameValues.put("lives", lives);
        gameValues.put("time", time);
        gameValues.put("wordTurns", wordTurns);

        myStage.setOnCloseRequest(event -> StageFlow.showStage("Menu"));
    }

    public void loadDataToCombo() {
        comboSelectCat.getItems().clear();
        for (Category category : Data.getCategories().toArray(Category[].class)) {
            comboSelectCat.getItems().addAll(category.getName());
        }
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
            final Node node = cardPane.getChildren().get(i);
            playerNames[i] = ((PlayerCardNode) node).getPlayerName();
        }

        Category category = Data.searchCategory(comboSelectCat.getValue());

        try {
            final FXMLLoader loader = loadFXML("GameBView");
            final Parent root = loader.load();

            final GameBController controller = loader.getController();

            final Stage gameStage = new Stage();
            gameStage.setScene(new Scene(root));
            gameStage.setTitle("Game");

            assert category != null;
            controller.initController(gameStage, gameValues, playerNames, category);

            myStage.close();
            gameStage.show();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
