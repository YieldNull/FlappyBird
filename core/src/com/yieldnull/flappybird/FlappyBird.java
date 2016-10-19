package com.yieldnull.flappybird;

import com.badlogic.gdx.Game;
import com.yieldnull.flappybird.render.Renderer;
import com.yieldnull.flappybird.screen.StartScreen;

public class FlappyBird extends Game {

    private Renderer renderer;

    @Override
    public void create() {
        renderer = new Renderer();

        setScreen(new StartScreen(renderer));
    }

    @Override
    public void dispose() {
        super.dispose();

        renderer.dispose();
    }
}
