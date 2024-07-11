package com.util;

import javafx.scene.image.Image;

import java.io.File;
import java.util.Objects;

public class Loader {
    public static Image loadImage(String path){
        try {
            return new Image(Objects.requireNonNull(Loader.class.getResourceAsStream(path)));
        } catch (Exception e) {
            System.out.println("Error loading image: " + path);
        }
        return null;
    }

    public static String loadSound(String path) {
        return Objects.requireNonNull(Loader.class.getResource(path)).toString();
    }
}
