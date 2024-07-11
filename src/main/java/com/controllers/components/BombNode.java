package com.controllers.components;

import com.util.Loader;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Random;

public class BombNode extends StackPane {
    private final ImageView bombImageView;
    private final ImageView starImageView;
    private final ParallelTransition starAnimation = new ParallelTransition();
    private final ScaleTransition bombAnimation = new ScaleTransition();

    public BombNode() {
        bombImageView = new ImageView(Loader.loadImage("/images/bomb.png"));
        starImageView = new ImageView(Loader.loadImage("/images/star.png"));
        starImageView.setVisible(false);

        starImageView.setFitWidth(30);
        starImageView.setFitHeight(30);

        initAnimations();

        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(0, 0, 15, 15));
        setMaxWidth(93);
        setMaxHeight(93);

        getChildren().addAll(bombImageView, starImageView);
    }

    private void initAnimations() {
        bombAnimation.setNode(bombImageView);
        bombAnimation.setDuration(Duration.seconds(0.3));
        bombAnimation.setCycleCount(ScaleTransition.INDEFINITE);

        bombAnimation.setToX(0.90);
        bombAnimation.setToY(0.90);

        final RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(starImageView);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setToAngle(360);

        rotateTransition.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                return new Random().nextDouble() * t;
            }
        });

        final ScaleTransition scaleTransitionStar = new ScaleTransition(Duration.seconds(0.35), starImageView);
        scaleTransitionStar.setCycleCount(ScaleTransition.INDEFINITE);

        scaleTransitionStar.setToX(0.7);
        scaleTransitionStar.setToY(0.7);

        starAnimation.getChildren().addAll(rotateTransition, scaleTransitionStar);
    }

    public void initBomb() {
        starImageView.setVisible(true);
        bombAnimation.play();
        starAnimation.play();
    }

    public void stopComponent() {
        bombImageView.setVisible(false);
        starImageView.setVisible(false);
        bombAnimation.stop();
        starAnimation.stop();
    }

    public void reset() {
        bombImageView.setVisible(true);
    }
}
