package com.controllers.components;

import com.controllers.util.Animations;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.util.Assets;
import com.util.Constants;
import com.util.Loader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerNode extends StackPane {
    private boolean isDeath;
    private boolean isPlaying;

    private final int maxLives;
    private final Label playerName;
    private final VBox playerInfo;
    private final SVGGlyph playerIcon;
    private final HBox playerLives = new HBox();
    private final Color RED = Color.valueOf("#4b2915");
    private final Color GREEN = Color.valueOf("#2f6f25");
    private final Label playerWord;
    private final HBox playerIconCont;
    private final ImageView cross;
    private final ImageView check;
    private final ImageView heartBreak;
    private final Circle circleEffect;

    public PlayerNode(String name, Pane paneLabels, int maxLives) {
        this.maxLives = maxLives;

        setPrefSize(Constants.PLAYER_W, Constants.PLAYER_W);

        /* Player Assets */
        cross = createImageView("/images/cross.png");
        check = createImageView("/images/check.png");
        heartBreak = createImageView("/images/heart-break.png");
        circleEffect = new Circle(1, GREEN);

        /* Player Info */
        playerInfo = new VBox();
        playerInfo.setAlignment(Pos.TOP_CENTER);
        playerInfo.getStyleClass().add("player-info");

        playerName = createLabel(name);

        playerIconCont = new HBox();
        playerIconCont.setAlignment(Pos.CENTER);
        playerIconCont.setPrefWidth(Constants.PLAYER_ICONT_SIZE);
        playerIconCont.setPrefHeight(Constants.PLAYER_ICONT_SIZE);
        playerIconCont.setMaxWidth(HBox.USE_PREF_SIZE);
        playerIconCont.getStyleClass().add("icon-container");

        playerIcon = SVGGlyphLoader.getGlyph("custom.icon-player-1");
        playerIcon.setSize(Constants.PLAYER_ICON_W, Constants.PLAYER_ICON_H);
        playerIcon.setFill(javafx.scene.paint.Color.valueOf("#575757"));

        playerIconCont.getChildren().add(playerIcon);
        playerInfo.getChildren().addAll(playerName, playerIconCont);

        playerWord = createLabel("ELECTROENCEFALOGRAFISTAAAAAA");

        /* Player lives */

        playerLives.getStyleClass().add("lives-cont");
        playerLives.setAlignment(Pos.CENTER);
        playerLives.setPrefWidth(Constants.PLAYER_LIVES_W);
        playerLives.setPrefHeight(Constants.PLAYER_LIVES_H);
        playerLives.setMaxWidth(HBox.USE_PREF_SIZE);
        playerLives.setMaxHeight(HBox.USE_PREF_SIZE);

        final Image heart = Loader.loadImage("/images/heart-icon.png");
        for (int i = 0; i < maxLives; i++) {
            playerLives.getChildren().add(new ImageView(heart));
        }

        cross.setVisible(false);
        check.setVisible(false);
        circleEffect.setVisible(false);
        heartBreak.setVisible(false);

        /* Add all to the StackPane Player */
        getChildren().addAll(playerInfo, playerLives, cross, check, circleEffect, heartBreak);

        /* Add the label to the paneLabels */
        paneLabels.getChildren().add(playerWord);

    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label-name");
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private ImageView createImageView(String path) {
        final ImageView imageView = new ImageView(Loader.loadImage(path));
        imageView.setVisible(false);
        return imageView;
    }

    public boolean isDead() {
        return isDeath;
    }

    public String getName() {
        return playerName.getText();
    }

    public StackPane getWinnerCard() {
        final StackPane winnerCard = new StackPane();
        final ImageView medal = new ImageView(Loader.loadImage("/images/medal.png"));
        final VBox winnerInfo = new VBox();
        final Label winnerName = new Label(getName());
        final Label message = new Label("Gano la ultima ronda!");

        winnerInfo.setAlignment(Pos.CENTER);
        winnerInfo.setSpacing(10);

        winnerName.getStyleClass().addAll("label-word", "label-winner");
        message.getStyleClass().add("label-message");

        playerIcon.setSize(Constants.WINNER_ICON_W, Constants.WINNER_ICON_H);
        playerIconCont.setPrefWidth(Constants.WINNER_ICONT_SIZE);
        playerIconCont.setPrefHeight(Constants.WINNER_ICONT_SIZE);
        playerIconCont.getStyleClass().add("icon-container-winner");

        medal.setFitHeight(Constants.MEDAL_SIZE);
        medal.setFitWidth(Constants.MEDAL_SIZE);

        winnerInfo.getChildren().addAll(playerIconCont, winnerName, message);
        winnerCard.getChildren().addAll(winnerInfo, medal);
        return winnerCard;
    }

    public void setActive(boolean active) {
        isPlaying = active;
        if (active) {
            playerWord.setDisable(false);
            playerWord.getStyleClass().remove("wrong-word");
        }
    }

    public void playAnswerAnimation(boolean correct) {
        if (correct) {
            circleEffect.setVisible(true);
            circleEffect.setFill(GREEN);
            check.setVisible(true);
            Animations.playCheckAnimation(playerIconCont);
            Animations.playRiseAnimation(check, () -> check.setVisible(false));
            Animations.playVanishAnimation(circleEffect, () -> circleEffect.setVisible(false));
            Assets.checkSound.play();
        } else {
            cross.setVisible(true);
            Animations.playShakeAnimation(playerIconCont);
            Animations.playRiseAnimation(cross, () -> cross.setVisible(false));
            Assets.incorrectSound.play();
        }
    }

    public void loseLife() {
        /* Animation */
        circleEffect.setVisible(true);
        circleEffect.setFill(RED);
        heartBreak.setVisible(true);
        Animations.playShakeAnimation(playerIconCont);
        Animations.playRiseAnimation(heartBreak, () -> heartBreak.setVisible(false));
        Animations.playVanishAnimation(circleEffect, () -> circleEffect.setVisible(false));

        /* Logical */
        playerLives.getChildren().remove(playerLives.getChildren().size() - 1);
        playerWord.setDisable(true);
        playerWord.getStyleClass().add("wrong-word");

        if (playerLives.getChildren().isEmpty()) {
            playerName.setDisable(true);
            isDeath = true;
            final ImageView skull = new ImageView(Loader.loadImage("/images/skull.png"));
            skull.setFitHeight(Constants.SKULL_SIZE);
            skull.setFitWidth(Constants.SKULL_SIZE);
            playerLives.getChildren().add(skull);
        }
    }

    public void reset() {
        if (isDeath)
            isDeath = false;
        else {
            playerIcon.setSize(Constants.PLAYER_ICON_W, Constants.PLAYER_ICON_H);
            playerIconCont.setPrefWidth(Constants.PLAYER_ICONT_SIZE);
            playerIconCont.setPrefHeight(Constants.PLAYER_ICONT_SIZE);
            playerInfo.getChildren().add(playerIconCont);
            playerIconCont.getStyleClass().remove("icon-container-winner");
        }
        isPlaying = false;
        playerName.setDisable(false);
        playerWord.setDisable(false);
        playerWord.setText("");
        playerWord.getStyleClass().remove("wrong-word");

        playerLives.getChildren().clear();
        final Image heart = Loader.loadImage("/images/heart-icon.png");

        for (int i = 0; i < maxLives; i++) {
            final ImageView img = new ImageView(heart);
            playerLives.getChildren().add(img);
        }
    }

    public void postInit(TextField inputToBind) {
        playerWord.setPrefWidth(playerWord.getWidth());
        playerWord.setText("");
        inputToBind.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isPlaying)
                playerWord.setText(newValue);
        });

        posWord();
    }

    public void posWord() {
        final Bounds bounds = getLayoutBounds();
        final Point2D point = localToScene(0, 0);
        final Point2D panePoint = playerWord.getParent().localToScene(0,0);

        playerWord.setLayoutX(
                point.getX() + (bounds.getWidth() / 2) - (playerWord.getWidth() / 2)
        );
        playerWord.setLayoutY((point.getY() - panePoint.getY()) + bounds.getHeight());
    }
}
