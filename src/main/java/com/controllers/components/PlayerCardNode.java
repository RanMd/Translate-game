package com.controllers.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.util.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.function.Supplier;

public class PlayerCardNode extends HBox {
    private final Pane father;
    private Label playerName;
    private final Supplier<String> changeName;

    public PlayerCardNode(Pane father, String name, Supplier<String> changeName) {
        this.father = father;
        this.changeName = changeName;
        getStyleClass().add("player-card");

        final HBox playerIconContainer = createHBox(Constants.PLAYER_CARD_H, null);

        final HBox playerIconFrame = createHBox(Constants.PLAYER_ICON_FRAME, "icon-frame");
        playerIconFrame.setMaxHeight(HBox.USE_PREF_SIZE);

        final SVGGlyph playerIcon = createIcon("custom.icon-player-1", Constants.PLAYER_ICON_SIZE);

        playerIconFrame.getChildren().add(playerIcon);
        playerIconContainer.getChildren().add(playerIconFrame);

        final VBox playerInfo = createPlayerInfo(name);
        getChildren().addAll(playerIconContainer, playerInfo);
    }

    private VBox createPlayerInfo(String name) {
        final VBox playerInfo = new VBox();
        playerInfo.setSpacing(5);
        playerInfo.setAlignment(Pos.CENTER_LEFT);
        playerInfo.getStyleClass().add("info-card");

        playerName = new Label(name);
        playerName.getStyleClass().add("player-name");
        playerName.setAlignment(Pos.CENTER);
        playerName.setPrefWidth(Constants.PLAYER_NAME);
        playerName.setMaxSize(Label.USE_PREF_SIZE, Double.MAX_VALUE);

        final HBox btnCont = new HBox();
        btnCont.setSpacing(10);
        btnCont.getChildren().addAll(createButton("custom.pencil", false), createButton("custom.delete", true));

        playerInfo.getChildren().addAll(playerName, btnCont);
        return playerInfo;
    }

    public JFXButton createButton(String iconName, boolean isDelete) {
        final JFXButton button = new JFXButton();
        button.getStyleClass().add("btn-card");
        button.setPrefWidth(Constants.PLAYER_BTN);
        button.setPrefHeight(Constants.PLAYER_BTN);

        final SVGGlyph icon = createIcon(iconName, Constants.PLAYER_BTN_ICON);
        button.setGraphic(icon);

        if (isDelete) button.setOnAction(event -> deleteCard());
        else button.setOnAction(event -> editName());

        return button;
    }

    private HBox createHBox(int size, String styleClass) {
        final HBox hBox = new HBox();
        hBox.setPrefWidth(size);
        hBox.setPrefHeight(size);
        hBox.setAlignment(Pos.CENTER);
        if (styleClass != null) {
            hBox.getStyleClass().add(styleClass);
        }
        return hBox;
    }

    private SVGGlyph createIcon(String iconName, int size) {
        final SVGGlyph icon;
        try {
            icon = SVGGlyphLoader.getGlyph(iconName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        icon.setSize(size);
        icon.setFill(javafx.scene.paint.Color.valueOf("#b3b3b3"));
        return icon;
    }

    private void deleteCard() {
        father.getChildren().remove(this);
    }

    public void editName() {
        final String name = changeName.get();
        if (name == null || name.isEmpty()) return;

        playerName.setText(name);
    }

    public String getPlayerName() {
        return playerName.getText();
    }

}
