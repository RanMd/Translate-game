package com.controllers.util;

import com.util.Constants;
import com.util.interfaces.InitFlowController;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.main.App.loadFXML;

public class StageFlow {
    private static final Map<String, Stage> stages;

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, UP_LEFT, DOWN_RIGHT, UP_RIGHT, DOWN_LEFT,
    }

    private static final double TIME = Constants.SWIPE_TIME;

    private static final ParallelTransition swipe = new ParallelTransition();
    private static final TranslateTransition swipeFrom = new TranslateTransition();
    private static final TranslateTransition swipeTo = new TranslateTransition();

    static {
        initSwipe();
        stages = new HashMap<>(1);
    }

    private static void initSwipe() {
        swipeFrom.setInterpolator(Interpolator.EASE_BOTH);
        swipeFrom.setDuration(javafx.util.Duration.seconds(TIME));
        swipeFrom.setOnFinished(event -> {
            swipeFrom.getNode().setVisible(false); // Hide the node
            swipeFrom.setNode(null);
        });

        swipeTo.setInterpolator(Interpolator.EASE_BOTH);
        swipeTo.setDuration(javafx.util.Duration.seconds(TIME));
        swipeTo.setOnFinished(event -> swipeTo.setNode(null));

        swipe.getChildren().add(swipeFrom);
        swipe.getChildren().add(swipeTo);
    }

    public static void playSwipe(Parent fromView, Parent toView, Direction direction) {
        toView.setVisible(true); // Show the node
        swipeFrom.setNode(fromView);
        swipeTo.setNode(toView);
        swipe.setOnFinished(event -> toView.toFront());

        setSwipeDirection(direction);

        swipe.play();
    }

    private static void setSwipeDirection(Direction direction) {
        double fromX = 0, toX = 0, fromY = 0, toY = 0;
        switch (direction) {
            case LEFT:
                fromX = -Constants.MENU_WIDTH;
                toX = Constants.MENU_WIDTH;
                break;
            case RIGHT:
                fromX = Constants.MENU_WIDTH;
                toX = -Constants.MENU_WIDTH;
                break;
            case UP:
                fromY = -Constants.MENU_HEIGHT;
                toY = Constants.MENU_HEIGHT;
                break;
            case DOWN:
                fromY = Constants.MENU_HEIGHT;
                toY = -Constants.MENU_HEIGHT;
                break;
            case UP_LEFT:
                toX = Constants.MENU_WIDTH;
                fromX = -Constants.MENU_WIDTH;
                fromY = -Constants.MENU_HEIGHT;
                toY = Constants.MENU_HEIGHT;
                break;
            case DOWN_RIGHT:
                toX = -Constants.MENU_WIDTH;
                fromX = Constants.MENU_WIDTH;
                fromY = Constants.MENU_HEIGHT;
                toY = -Constants.MENU_HEIGHT;
                break;
            case UP_RIGHT:
                fromX = Constants.MENU_WIDTH;
                toX = -Constants.MENU_WIDTH;
                fromY = -Constants.MENU_HEIGHT;
                toY = Constants.MENU_HEIGHT;
                break;
            case DOWN_LEFT:
                fromX = -Constants.MENU_WIDTH;
                toX = Constants.MENU_WIDTH;
                fromY = Constants.MENU_HEIGHT;
                toY = -Constants.MENU_HEIGHT;
                break;
        }

        swipeFrom.setToX(fromX);
        swipeTo.setFromX(toX);
        swipeTo.setToX(0);

        swipeFrom.setToY(fromY);
        swipeTo.setFromY(toY);
        swipeTo.setToY(0);
    }

    public static <T extends InitFlowController> Parent loadStage(String fxmlName, Runnable returnToMenu) {
        try {
            final FXMLLoader loader = loadFXML(fxmlName);
            final Parent root = loader.load();
            T controller = loader.getController();
            controller.initController(returnToMenu);
            return root;
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    public static void addStageFlow(String stageName, Stage stage) {
        stages.put(stageName, stage);
    }

    public static void showStage(String key) {
        stages.get(key).show();
    }

    public static void hideStage(String key) {
        stages.get(key).hide();
    }

    public static void initClass() {
        // Initialize the class
    }
}
