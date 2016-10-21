package com.yieldnull.flappybird;

import com.badlogic.gdx.Game;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.screen.GameScreen;

public class FlappyBird extends Game {

    @Override
    public void create() {

        Assets.load();
        setScreen(new GameScreen());
    }


    @Override
    public void dispose() {
        super.dispose();

        Assets.dispose();
    }
}
