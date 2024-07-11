package com.controllers.util;

import com.util.Constants;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import static javafx.animation.Animation.Status.RUNNING;

public class Animations {
    private final static SequentialTransition shakingAnimation = new SequentialTransition();
    private final static SequentialTransition swipeAnimation = new SequentialTransition();
    private final static ParallelTransition checkAnimation = new ParallelTransition();
    private final static ParallelTransition verifyAnimation = new ParallelTransition();
    private final static ParallelTransition vanishAnimation = new ParallelTransition();
    private final static ParallelTransition explosionAnimation = new ParallelTransition();
    private final static ParallelTransition counterAnimation = new ParallelTransition();

    static {
        initShakeAnimation();
        initVerifyAnimations();
        initCheckAnimation();
        initVanishAnimation();
        initExplosionAnimation();
        initCounterAnimation();
        initSwipeAnimation();
    }

    private static void initSwipeAnimation() {
        final double TIME = 0.5;
        final double DELTAX = Constants.MENU_WIDTH;

        final TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(TIME));
        translateTransition.setFromX(DELTAX);
        translateTransition.setToX(0);

        final TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(TIME));
        translateTransition1.setFromX(0);
        translateTransition1.setToX(-DELTAX);
        translateTransition1.setDelay(Duration.seconds(2));

        swipeAnimation.setOnFinished(event -> swipeAnimation.setNode(null));
        swipeAnimation.getChildren().addAll(translateTransition, translateTransition1);
    }

    private static void initCounterAnimation() {
        final double TIME = 0.75;
        final double FROM_SCALE = 8;
        final double SCALE = 20;

        final FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TIME));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        final ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(TIME));
        scaleTransition.setFromX(FROM_SCALE);
        scaleTransition.setFromY(FROM_SCALE);
        scaleTransition.setToX(SCALE);
        scaleTransition.setToY(SCALE);

        counterAnimation.setOnFinished(event -> counterAnimation.setNode(null));
        counterAnimation.getChildren().addAll(scaleTransition, fadeTransition);
    }

    private static void initShakeAnimation() {
        final double TIME = 0.05;
        final double[] DELTAS = {-14, 23, -18, 13, -8, 8, -4};
        final TranslateTransition[] transitionsShake = new TranslateTransition[DELTAS.length];

        for (int i = 0; i < DELTAS.length; i++) {
            transitionsShake[i] = createTranslateTransition(DELTAS[i], Duration.seconds(TIME));
        }

        shakingAnimation.setOnFinished(event -> shakingAnimation.setNode(null));
        shakingAnimation.getChildren().addAll(transitionsShake);
    }

    private static void initVanishAnimation() {
        final double TIME = 0.35;
        final double FROM_SCALE = 35;
        final double SCALE = 70;

        final FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TIME));
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(0);

        final ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(TIME));
        scaleTransition.setFromX(FROM_SCALE);
        scaleTransition.setFromY(FROM_SCALE);
        scaleTransition.setToX(SCALE);
        scaleTransition.setToY(SCALE);

        vanishAnimation.setOnFinished(event -> vanishAnimation.setNode(null));
        vanishAnimation.getChildren().addAll(scaleTransition, fadeTransition);
    }

    private static void initCheckAnimation() {
        final double TIME = 0.16;
        final RotateTransition rotateTransition = new RotateTransition(Duration.seconds(TIME));
        rotateTransition.setFromAngle(-30);
        rotateTransition.setToAngle(0);

        final ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(TIME));
        scaleTransition.setFromX(1.25);
        scaleTransition.setFromY(1.25);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        checkAnimation.setOnFinished(event -> checkAnimation.setNode(null));
        checkAnimation.getChildren().addAll(rotateTransition, scaleTransition);
    }

    private static void initVerifyAnimations() {
        final TranslateTransition rise = new TranslateTransition();
        final FadeTransition fade = new FadeTransition();
        final ScaleTransition scale = new ScaleTransition();
        final double TIME = 0.35;

        rise.setDuration(Duration.seconds(TIME));
        rise.setByY(-50);

        fade.setDuration(Duration.seconds(TIME));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        scale.setDuration(Duration.seconds(TIME));
        scale.setToX(1.5);
        scale.setToY(1.5);

        verifyAnimation.setOnFinished(event -> verifyAnimation.setNode(null));
        verifyAnimation.getChildren().addAll(rise, fade, scale);
    }

    private static void initExplosionAnimation() {
        final double TIME = 0.3;
        final double FROM_SCALE = 1;
        final double SCALE = 0.5;

        final FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TIME));
        fadeTransition.setFromValue(0.75);
        fadeTransition.setToValue(0);

        final ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(TIME));
        scaleTransition.setFromX(FROM_SCALE);
        scaleTransition.setFromY(FROM_SCALE);
        scaleTransition.setToX(SCALE);
        scaleTransition.setToY(SCALE);

        final SequentialTransition sequentialRotate = new SequentialTransition();

        final RotateTransition rotateTransition = new RotateTransition(Duration.seconds(TIME / 3));
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(90);

        final RotateTransition rotateTransition1 = new RotateTransition(Duration.seconds(TIME / 3));
        rotateTransition1.setToAngle(30);

        final RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(TIME / 3));
        rotateTransition2.setToAngle(75);

        sequentialRotate.getChildren().addAll(rotateTransition, rotateTransition1, rotateTransition2);

        explosionAnimation.setOnFinished(event -> explosionAnimation.setNode(null));
        explosionAnimation.getChildren().addAll(scaleTransition, fadeTransition, sequentialRotate);
    }

    private static TranslateTransition createTranslateTransition(double deltaX, Duration duration) {
        final TranslateTransition transition = new TranslateTransition(duration);
        transition.setByX(deltaX);
        return transition;
    }

    public static void playCounterAnimation(Node node) {
        counterAnimation.setNode(node);
        counterAnimation.play();
    }

    public static void playShakeAnimation(Node node) {
        if (shakingAnimation.getStatus() == RUNNING) {
            return;
        }

        shakingAnimation.setNode(node);
        shakingAnimation.play();
    }

    public static void playCheckAnimation(Node node) {
        checkAnimation.setNode(node);
        checkAnimation.play();
    }

    public static void playVanishAnimation(Node node, Runnable onFinished) {
        vanishAnimation.setNode(node);
        vanishAnimation.setOnFinished(event -> {
            onFinished.run();
            vanishAnimation.setNode(null);
        });
        vanishAnimation.play();
    }

    public static void playRiseAnimation(Node node, Runnable onFinished) {
        if (verifyAnimation.getStatus() == RUNNING) {
            if (verifyAnimation.getNode() == node) {
                return;
            }
            verifyAnimation.getOnFinished().handle(null);
            verifyAnimation.stop();
        }

        verifyAnimation.setNode(node);
        verifyAnimation.setOnFinished(event -> {
            node.setTranslateY(0);
            node.setScaleX(1);
            node.setScaleY(1);
            onFinished.run();
            verifyAnimation.setNode(null);
        });

        verifyAnimation.play();
    }

    public static void playExplosionAnimation(Node node, Runnable onFinished) {
        explosionAnimation.setNode(node);
        explosionAnimation.setOnFinished(event -> {
            onFinished.run();
            explosionAnimation.setNode(null);
        });
        explosionAnimation.play();
    }

    public static void playSwipeAnimation(Node node, Runnable onFinished) {
        swipeAnimation.setNode(node);
        swipeAnimation.play();
        swipeAnimation.setOnFinished(event -> {
            onFinished.run();
            swipeAnimation.setNode(null);
        });
    }

    public static void initClass() {
        // Empty method to initialize the class
    }
}
