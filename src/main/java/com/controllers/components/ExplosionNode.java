package com.controllers.components;

import com.controllers.util.Animations;
import com.util.Assets;
import com.util.Loader;
import javafx.scene.image.ImageView;

public class ExplosionNode extends ImageView {

    public ExplosionNode() {
        super(Loader.loadImage("/images/explosion.png"));
        setVisible(false);
    }

    public void explode() {
        setVisible(true);
        Assets.boomSound.play();
        Animations.playExplosionAnimation(this, () -> setVisible(false));
    }
}
