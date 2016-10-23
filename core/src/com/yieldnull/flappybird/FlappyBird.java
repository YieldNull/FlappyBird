package com.yieldnull.flappybird;

import com.badlogic.gdx.Game;
import com.yieldnull.flappybird.screen.LauncherScreen;
import com.yieldnull.flappybird.util.Assets;

public class FlappyBird extends Game {

    @Override
    public void create() {

        Assets.load();
        setScreen(new LauncherScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();

        Assets.dispose();
    }
}
