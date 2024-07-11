package com.util;

import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Assets {
    public static final AudioClip lighterSound;
    public static final AudioClip boomSound;
    public static final AudioClip checkSound;
    public static final AudioClip incorrectSound;
    public static final AudioClip tri;
    public static final AudioClip tu;
    public static final AudioClip wan;
    public static final AudioClip gogogo;
    public static final AudioClip yeySound;
    public static final AudioClip clickSound;
    public static final MediaPlayer tickTackTrack;
    public static final MediaPlayer soundTrack;

    static {
        lighterSound = new AudioClip(Loader.loadSound("/sounds/lighter.m4a"));
        checkSound = new AudioClip(Loader.loadSound("/sounds/check.m4a"));
        checkSound.setVolume(0.5);
        incorrectSound = new AudioClip(Loader.loadSound("/sounds/incorrect.m4a"));
        boomSound = new AudioClip(Loader.loadSound("/sounds/boom.m4a"));
        tri = new AudioClip(Loader.loadSound("/sounds/3.m4a"));
        tu = new AudioClip(Loader.loadSound("/sounds/2.m4a"));
        wan = new AudioClip(Loader.loadSound("/sounds/1.m4a"));
        gogogo = new AudioClip(Loader.loadSound("/sounds/gogogo.m4a"));
        yeySound = new AudioClip(Loader.loadSound("/sounds/yey.m4a"));
        clickSound = new AudioClip(Loader.loadSound("/sounds/click.m4a"));
        tri.setVolume(0.3);
        tu.setVolume(0.3);
        wan.setVolume(0.3);
        gogogo.setVolume(0.3);
        clickSound.setVolume(0.5);

        /* MEDIA PLAYER */
        final Media media = new Media(Loader.loadSound("/sounds/tick-tack.m4a"));
        tickTackTrack = new MediaPlayer(media);

        final Media media2 = new Media(Loader.loadSound("/sounds/soundtrack.mp3"));
        soundTrack = new MediaPlayer(media2);
        soundTrack.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static void addSoundToButton(Button button, Runnable action) {
        button.setOnAction(event -> {
            clickSound.play();
            action.run();
        });
    }

    public static void initClass() {
        // Empty method to initialize the class
    }
}
