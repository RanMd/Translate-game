package com.controllers;

import com.controllers.util.Animations;
import com.controllers.util.StageFlow;
import com.models.Category;
import com.models.Player;
import com.models.TranslateGame;
import com.models.WordPair;
import com.util.Assets;
import com.util.Data;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class GameAController implements Initializable {
    private Timeline timeline;
    private int timeSeconds = 0;
    private TranslateGame game;
    private VBox winnerAlert;

    private final int MAX_CHARACTERS = 25;
    private final GaussianBlur gaussianBlur = new GaussianBlur();

    @FXML
    private Label labelCategory;

    @FXML
    private Label labelPlayer;

    @FXML
    private Label labelTimer;

    @FXML
    private Label labelWord;

    @FXML
    private VBox contInput;

    @FXML
    private TextField input;

    @FXML
    private StackPane stackContainer;

    @FXML
    private HBox playerCont;

    @FXML
    private VBox infoPlayer;

    @FXML
    private Label correctLabel;

    @FXML
    private VBox correctWordCont;

    @FXML
    private VBox content;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTimer.setText("1");
        correctWordCont.setVisible(false);

        stackContainer.heightProperty().addListener((obs, oldVal, newVal) ->
                contInput.setMaxHeight(newVal.doubleValue() * 0.20));

        contInput.widthProperty().addListener((obs, oldVal, newVal) ->
                input.setMaxWidth(newVal.doubleValue() * 0.75));

        final UnaryOperator<TextFormatter.Change> filter = change -> {
            final String text = change.getText();
            if (!text.matches("[a-zA-Z]*") || change.getControlNewText().length() > MAX_CHARACTERS)
                return null;

            change.setText(text.toUpperCase());
            return change;
        };

        input.setTextFormatter(new TextFormatter<>(filter));
        input.setOnKeyPressed(this::pressEnter);
    }

    private void pressEnter(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;
        changePlayer();
    }

    public void initController(Stage stage) {
        String[] playersArray = new String[2];

        for (int i = 0; i < playersArray.length; i++) {
            playersArray[i] = "Jugador " + (i + 1);
        }

        final Category category = Objects.requireNonNull(Data.getRandomCategory());
        game = new TranslateGame(createPlayers(playersArray), category, 3);
        initTimer(category);
    }

    public void initController(Stage stage, String[] playersName, Category category, int maxRounds) {
        game = new TranslateGame(createPlayers(playersName), category, maxRounds);

        initTimer(category);
    }

    private void initTimer(Category category) {
        actPlayer();
        actWord();

        labelCategory.setText(category.getName());

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds++;
            labelTimer.setText(Integer.toString(timeSeconds));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private Player[] createPlayers(String[] playersName) {
        final Player[] players = new Player[playersName.length];
        for (int i = 0; i < playersName.length; i++) {
            players[i] = new Player(playersName[i]);
        }
        return players;
    }

    public void resetTimer() {
        timeSeconds = 0;
        labelTimer.setText("1");
        timeline.playFromStart();
    }

    public void actPlayer() {
        labelPlayer.setText(game.getCurrentPlayer().getNombre());
    }

    public void actWord() {
        game.changeWord();
        labelWord.setText(game.getCurrentWord().getWord());
    }

    public void changePlayer() {
        final boolean isCorrect = game.getCurrentWord().checkTranslate(input.getText());
        if (!isCorrect) {
            input.setDisable(true);
            infoPlayer.setEffect(gaussianBlur);
            Animations.playShakeAnimation(playerCont); // esto queda pa arreglar debe ser el player no el cont
            correctWordCont.setVisible(true);
            correctLabel.setText(game.getCurrentWord().getCorrectWord());

            Animations.playSwipeAnimation(correctWordCont, () -> {
                input.setDisable(false);
                infoPlayer.setEffect(null);
                correctWordCont.setVisible(false);
                actWord();
            });
        }

        game.playerAnswered(isCorrect, timeSeconds);
        if (isCorrect) actWord();
        input.clear();

        if (!game.isEndGame()) {
            game.nextPlayerTurn();

            actPlayer();
            resetTimer();
        } else choseWinner();

    }

    private void choseWinner() {
        timeline.stop();
        changeWinnerAlert();
    }

    private void changeWinnerAlert() {
        if (winnerAlert == null) {
            try {
                final FXMLLoader loader = loadFXML("WinnerAlert");
                final Parent root = loader.load();
                winnerAlert = (VBox) root;
                final WinnerAlertController controller = loader.getController();

                final Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Game");

                controller.initController(stage, game.playerWinner(), this::backToGame);
                stackContainer.getChildren().add(winnerAlert);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

        StageFlow.playSwipe(content, winnerAlert, StageFlow.Direction.DOWN);
    }

    private void backToGame() {
        StageFlow.playSwipe(winnerAlert, content, StageFlow.Direction.UP);
    }
}
