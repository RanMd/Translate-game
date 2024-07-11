package com.util;

public class Constants {
    /* Game B Constants */
    public final static int ARROW_SIZE = 169;

    /* PLayerNode Constants */

    public final static int PLAYER_W = 90;
    public final static int PLAYER_ICON_W = 20;
    public final static int PLAYER_ICON_H = 23;
    public final static int PLAYER_ICONT_SIZE = 40;
    public final static int PLAYER_LIVES_W = 80;
    public final static int PLAYER_LIVES_H = 22;
    public final static int SKULL_SIZE = 27;
    public final static int WINNER_ICONT_SIZE = 96;
    public final static int WINNER_ICON_W = 45;
    public final static int WINNER_ICON_H = 50;
    public final static int MEDAL_SIZE = 32;

    /* PlayerCar Constants */

    public final static int PLAYER_CARD_H = 64;
    public final static int PLAYER_ICON_FRAME = 44;
    public final static int PLAYER_ICON_SIZE = 24;
    public final static int PLAYER_BTN = 26;
    public final static int PLAYER_BTN_ICON = 13;
    public final static double PLAYER_NAME = 75;

    /* Menu UI Constants */
    public final static double SWIPE_TIME = 0.4;
    public final static int MENU_WIDTH = 600;
    public final static int MENU_HEIGHT = 400;

    /*try {
            FXMLLoader loader = loadFXML(fxmlName);
            Parent root = loader.load();

            T controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

            controller.initController(stage);

            stage.show();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }*/
}
