package com.controllers;

import com.controllers.components.BombNode;
import com.controllers.components.ExplosionNode;
import com.controllers.components.PlayerNode;
import com.controllers.util.Animations;
import com.controllers.util.StageFlow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.models.Category;
import com.models.WordPair;
import com.util.Constants;
import fr.brouillard.oss.cssfx.CSSFX;
import com.util.Assets;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

public class GameBController implements Initializable {
    private int currentPlayer;
    private int survivors;
    private int grades;
    private int maxWordTurns;
    private int wordTurns = 0;
    private PlayerNode[] players;
    private Stage myStage;
    private WordPair[] words;
    private WordPair currentWord;

    private final Timeline timer = new Timeline();
    private final ExplosionNode explosion = new ExplosionNode();
    private final RotateTransition rotate_arrow = new RotateTransition();

    private final SVGGlyph arrow = SVGGlyphLoader.getGlyph("custom.arrow");
    private final Random rnd = new Random();
    private final BombNode bomb = new BombNode();

    private final int MAX_CHARACTERS = 25;

    @FXML
    private VBox windowGame;
    @FXML
    private HBox contInput; // 72
    @FXML
    private TextField input;
    @FXML
    private CircularPane playersPane;
    @FXML
    private StackPane stackGame;
    @FXML
    private Pane paneLabels;
    @FXML
    private VBox contWord; // 59
    @FXML
    private Label wordLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* Inits */
        initEffects();

        /* Add components */
        stackGame.getChildren().addAll(arrow, bomb, explosion);

        /* Config widths and heights */
        windowGame.heightProperty().addListener((obs, oldVal, newVal) -> {
                    contInput.setMinHeight(newVal.doubleValue() * 0.12);
                    contWord.setMinHeight(newVal.doubleValue() * 0.085);
                    posPlayerWord();
            }
        );

        windowGame.widthProperty().addListener((obs, oldVal, newVal) -> posPlayerWord());

        contInput.heightProperty().addListener((obs, oldVal, newVal) ->
                input.setMaxHeight(newVal.doubleValue() * 0.60));

        contInput.widthProperty().addListener((obs, oldVal, newVal) ->
                input.setMaxWidth(newVal.doubleValue() * 0.50));

        /* Config input */
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (!text.matches("[a-zA-Z]*") || change.getControlNewText().length() > MAX_CHARACTERS)
                return null;

            change.setText(text.toUpperCase());
            return change;
        };

        input.setTextFormatter(new TextFormatter<>(filter));
        input.setOnKeyReleased(this::pressEnter);
        input.setDisable(true);

        Platform.runLater(this::postInitPlayers);

        CSSFX.start();
    }

    public void initController(Stage myStage, Map<String, Integer> gameValues, String[] playerNames, Category category) {
        this.myStage = myStage;
        words = category.getWordsArray();
        initPLayers(playerNames, gameValues.get("lives"),gameValues.get("wordTurns"));
        initTimer(gameValues.get("time"));
    }

    public void initController(Stage myStage, Category category) {
        this.myStage = myStage;

        String[] names = new String[2];

        for (int i = 0; i < names.length; i++) {
            names[i] = "Jugador " + (i + 1);
        }

        words = category.getWordsArray();
        initPLayers(names, 3,3);
        initTimer(10);
    }

    /* Init methods */
    private void initPLayers(String[] playerNames, int maxLives,int maxWordTurns) {
        this.maxWordTurns = maxWordTurns;
        survivors = playerNames.length;
        players = new PlayerNode[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new PlayerNode(playerNames[i], paneLabels, maxLives);
            playersPane.add(players[i]);
        }

        grades = 360 / playerNames.length;

        switch (playerNames.length) {
            case 3:
                playersPane.setStartAngle(30.0);
                playersPane.setGap(215.0);
                StackPane.setMargin(playersPane, new Insets(0, 0, 0, 85));
                break;
            case 4:
                playersPane.setStartAngle(45.0);
                playersPane.setGap(160.0);
                break;
            case 5:
                playersPane.setStartAngle(54.0);
                playersPane.setGap(125.0);
                StackPane.setMargin(playersPane, new Insets(0, 0, 0, 38));
                break;
        }

        chosePlayer(players.length);
    }

    private void initEffects() {
        arrow.setSize(Constants.ARROW_SIZE);
        arrow.setFill(Color.valueOf("#eecb27"));
        rotate_arrow.setDuration(Duration.seconds(0.2));
        rotate_arrow.setNode(arrow);
    }

    private void initTimer(int time) {
        timer.getKeyFrames().add(new javafx.animation.KeyFrame(Duration.seconds(time), e -> looseTurn()));
    }

    private void initCounter() {
        wordLabel.setText("PREPARATE!");
        final double TIME = 0.85;
        final Label countdownLabel = new Label("3");

        final Timeline countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            stackGame.getChildren().add(countdownLabel);
            Animations.playCounterAnimation(countdownLabel);
            Assets.tri.play();
        }), new KeyFrame(Duration.seconds(TIME * 2), e -> {
            countdownLabel.setText("2");
            Animations.playCounterAnimation(countdownLabel);
            Assets.tu.play();
        }), new KeyFrame(Duration.seconds(TIME * 3), e -> {
            countdownLabel.setText("1");
            Animations.playCounterAnimation(countdownLabel);
            Assets.wan.play();
        }), new KeyFrame(Duration.seconds(TIME * 4), e -> {
            stackGame.getChildren().remove(countdownLabel);
            input.setDisable(false);
            Assets.gogogo.play();
        }));

        countdownTimeline.setOnFinished(e -> initGame());
        countdownTimeline.play();
    }

    /* Game methods */
    private void chosePlayer(int numPlayers) {
        final int[] rotations = new int[numPlayers];
        for (int i = 1; i < numPlayers; i++) {
            rotations[i] = i * grades;
        }

        currentPlayer = rnd.nextInt(numPlayers);
        arrow.setRotate(rotations[currentPlayer]);
        players[currentPlayer].setActive(true);
    }

    private void choseWord() {
        currentWord = words[rnd.nextInt(words.length)];
        wordLabel.setText(currentWord.getWord());
    }

    private void looseTurn() {
        explosion.explode();
        players[currentPlayer].looseLife();
        if (players[currentPlayer].isDead()) survivors--;

        if (survivors != 1) {
            nextPlayerTurn(true);
        } else choseWinner();
    }

    private void pressEnter(KeyEvent event) {
        if (input.getText().isEmpty()) return;

        if (event.getCode() != KeyCode.ENTER) {
            return;
        }

        final boolean isCorrect = currentWord.checkTranslate(input.getText());
        players[currentPlayer].playAnswerAnimation(isCorrect);

        if (!isCorrect) input.setText("");
        else nextPlayerTurn(false);
    }

    private void nextPlayerTurn(boolean looseTurn) {
        if (looseTurn) {
            wordTurns++;
            if (wordTurns == maxWordTurns) {
                choseWord();
                wordTurns = 0;
            }
        } else choseWord();

        timer.playFromStart();
        players[currentPlayer].setActive(false);
        input.setText("");

        currentPlayer = (currentPlayer + 1) % players.length;

        double rotation = grades;

        while (players[currentPlayer].isDead()) {
            rotation += grades;
            currentPlayer = (currentPlayer + 1) % players.length;
        }

        players[currentPlayer].setActive(true);
        rotate_arrow.setToAngle(arrow.getRotate() + rotation);
        rotate_arrow.play();

        Assets.tickTackTrack.stop();
        Assets.tickTackTrack.play();
    }

    private void choseWinner() {
        while (players[currentPlayer].isDead()) {
            currentPlayer = (currentPlayer + 1) % players.length;
        }

        stopComponents();

        wordLabel.setText("GANADOR!");

        final JFXButton btnRetry = new JFXButton("Volver a jugar");
        final JFXButton btnMenu = new JFXButton("Configuraciones");
        final JFXButton btnExit = new JFXButton("Salir");

        btnRetry.setFocusTraversable(false);
        btnMenu.setFocusTraversable(false);
        btnExit.setFocusTraversable(false);
        btnRetry.setButtonType(JFXButton.ButtonType.RAISED);
        btnMenu.setButtonType(JFXButton.ButtonType.RAISED);
        btnExit.setButtonType(JFXButton.ButtonType.RAISED);
        btnRetry.setCursor(javafx.scene.Cursor.HAND);
        btnMenu.setCursor(javafx.scene.Cursor.HAND);
        btnExit.setCursor(javafx.scene.Cursor.HAND);

        btnRetry.getStyleClass().add("btn");
        btnMenu.getStyleClass().addAll("btn", "btn-exit");
        btnExit.getStyleClass().addAll("btn", "btn-exit");

        btnRetry.setOnAction(e -> resetGame());
        btnMenu.setOnAction(e -> changeToMenu());
        btnExit.setOnAction(e -> Platform.exit());

        stackGame.getChildren().add(players[currentPlayer].getWinnerCard());
        contInput.getChildren().addAll(btnRetry, btnMenu, btnExit);
        Assets.yeySound.play();
    }

    private void stopComponents() {
        input.setDisable(true);
        Assets.tickTackTrack.stop();
        timer.stop();
        bomb.stopComponent();
        arrow.setVisible(false);
        playersPane.setVisible(false);
        paneLabels.setVisible(false);
        contInput.getChildren().remove(0);
    }

    public void resetGame() {
        arrow.setVisible(true);
        playersPane.setVisible(true);
        paneLabels.setVisible(true);
        input.setText("");
        contInput.getChildren().clear();
        contInput.getChildren().add(input);
        stackGame.getChildren().remove(stackGame.getChildren().size() - 1);

        for (PlayerNode player : players) {
            player.reset();
        }
        bomb.reset();

        survivors = players.length;
        chosePlayer(players.length);
        choseWord();
        initCounter();
    }

    /* Post init methods */

    private void initGame() {
        choseWord();
        bomb.initBomb();
        Assets.lighterSound.play();
        Assets.tickTackTrack.play();
        timer.play();
    }

    private void postInitPlayers() {
        for (PlayerNode player : players) {
            player.postInit(input);
        }

        initCounter();
    }

    public void posPlayerWord() {
        for (PlayerNode player : players) {
            Platform.runLater(player::posPlayerWord);
        }
    }

    private void changeToMenu() {
        StageFlow.showStage("Menu");
        myStage.close();
    }

}





