package com.main;

import com.controllers.GameAController;
import com.controllers.GameBController;
import com.controllers.MenuViewController;
import com.controllers.util.Animations;
import com.controllers.util.StageFlow;
import com.jfoenix.svg.SVGGlyphLoader;
import com.util.Assets;
import com.util.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void init() {
        Animations.initClass();
        StageFlow.initClass();
        Assets.initClass();
        Data.initClass();
        try {
            SVGGlyphLoader.loadGlyphsFont(getClass().getResourceAsStream("/fonts/icons.svg"), "custom");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException{
        final FXMLLoader loader = loadFXML("GameBView");
        final Parent root = loader.load();

        GameBController controller = loader.getController();
        controller.initController(stage, Data.getRandomCategory());
//        GameController controller = loader.getController();
//        controller.initController(stage, Data.getRandomCategory());

        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Menu");
        stage.show();
    }

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource("/views/" + fxml + ".fxml"));
    }

    public static void main(String[] args) {
        launch();
    }

}
