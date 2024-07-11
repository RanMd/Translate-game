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
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.main.App.loadFXML;

public class GameAController implements Initializable {
    private Stage myStage;
    private Timeline timeline;
    private int timeSeconds = 0;
    private TranslateGame game;
    private Category categoryGame;
    private WordPair word;
    private int turn;
    private int cantRounds;
    private int maxRounds;
    private final ArrayList<String> names = new ArrayList<>();
    private final int MAX_CHARACTERS = 25;
    private final GaussianBlur gaussianBlur = new GaussianBlur();
    private final Random rnd = new Random();

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
    private StackPane windowGame;

    @FXML
    private HBox playerCont;

    @FXML
    private VBox correctWord;

    @FXML
    private VBox infoPlayer;

    @FXML
    private Label correctLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cantRounds = 0;
        labelTimer.setText("1");
        turn = 0;
        startTimer();
        game = new TranslateGame();
        correctWord.setVisible(false);

        windowGame.heightProperty().addListener((obs, oldVal, newVal) ->
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
        if (event.getCode() != KeyCode.ENTER) {
            return;
        }
        changePlayer();
    }

    public void initController(Stage stage) {
        this.myStage = stage;
        this.maxRounds = 5;
        this.categoryGame = Data.getRandomCategory();
        final int players = rnd.nextInt(4) + 2;

        for (int i = 0; i < players; i++) {
            final Player jugador = new Player("Jugador " + (i + 1));
            game.addPlayer(jugador);
            names.add(jugador.getNombre());
        }

        labelCategory.setText(categoryGame.getName());
        actPlayer();
        word = categoryGame.getRandomWord();
        actWord();
    }

    public void initController(Stage stage, ArrayList<String> playersName, Category category, int maxRounds) {
        this.myStage = stage;
        this.maxRounds = maxRounds;
        this.categoryGame = category;

        for (String playerName : playersName) {
            final Player jugador = new Player(playerName);
            game.addPlayer(jugador);
            names.add(playerName);
        }

        labelCategory.setText(categoryGame.getName());
        actPlayer();
        word = categoryGame.getRandomWord();
        actWord();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds++;
            labelTimer.setText(Integer.toString(timeSeconds));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void resetTimer() {
        stopTimer();
        timeSeconds = 0;
        labelTimer.setText("0");
        System.out.println("Timer reset to: " + formatTime(timeSeconds));
        startTimer();
    }

    public void actPlayer() {
        Player jugador = game.getPlayers().get(turn);
        labelPlayer.setText(jugador.getNombre());
    }

    public void actWord() {
        labelWord.setText(word.getWord());
    }

    public void changePlayer() {
        if (cantRounds != -1) {
            if (word.checkTranslate(input.getText())) {
                game.getPlayers().get(turn).setPuntajeTiempo(timeSeconds);

                if (turn >= game.getPlayers().size() - 1) {
                    turn = 0;
                    cantRounds++;
                }
                else if(turn < game.getPlayers().size() - 1) {
                    turn++;
                }
                if (cantRounds >= maxRounds) {
                    stopTimer();
                    cantRounds--;
                    changeWinnerAlert();

                } else {
                    word = categoryGame.getRandomWord();
                    input.setText("");
                    actWord();
                    resetTimer();
                    actPlayer();
                }
                actPlayer();
            } else {
                game.getPlayers().get(turn).incrementarErrores();
                input.setText("");
                input.setDisable(true);
                infoPlayer.setEffect(gaussianBlur);
                Animations.playShakeAnimation(playerCont);
                Assets.incorrectSound.play();
                correctWord.setVisible(true);
                correctLabel.setText(word.getCorrectWord());

                Animations.playSwipeAnimation(correctWord, () -> {
                    input.setDisable(false);
                    infoPlayer.setEffect(null);
                    correctWord.setVisible(false);
                    actWord();
                });
                actPlayer();
            }
            actPlayer();
        } else {
           changeWinnerAlert();
        }

    }


    private void showPoints() {
        final StringBuilder points = new StringBuilder();
        for (Player jugador : game.getPlayers()) {
            points.append(jugador.getNombre()).append(": ").append("Tiempo acumulado: "+jugador.getPuntajeTiempo()).append("  Errores:  "+jugador.getErrores()).append("\n");
        }

        final Player ganador = game.playerWinner();
        String mensajeFinal = points.toString() + "\nGanador con menor tiempo: " + (ganador != null ? ganador.getNombre() : "N/A")+"\n Cantidad de errores: "+(ganador != null ? ganador.getErrores() : "N/A");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Puntajes Finales");
        alert.setHeaderText(null);
        alert.setContentText(mensajeFinal);

        ButtonType okButton = new ButtonType("SALIR AL MENU", ButtonBar.ButtonData.OK_DONE);
        ButtonType repetirButton = new ButtonType("REPETIR", ButtonBar.ButtonData.OK_DONE);

        alert.getButtonTypes().setAll(okButton, repetirButton);

        Button ok = (Button) alert.getDialogPane().lookupButton(okButton);
        ok.setOnAction(event -> {
            changeToMenu();
            alert.close();
        });

        Button repetir = (Button) alert.getDialogPane().lookupButton(repetirButton);
        repetir.setOnAction(event -> {
            reset();
            alert.close();
        });

        alert.showAndWait();
    }


    private void changeToMenu() {
        StageFlow.showStage("Menu");
        myStage.close();
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

    private void changeWinnerAlert() {
        try {

            final FXMLLoader loader = loadFXML("WinnerAlert");
            final Parent root = loader.load();

            final WinnerAlertController controller = loader.getController();

            final Stage gameStage = new Stage();
            gameStage.setScene(new Scene(root));
            gameStage.setTitle("Game");

            controller.initController(gameStage, names,game.playerWinner() ,categoryGame, maxRounds);

            myStage.close();
            gameStage.show();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }






}
