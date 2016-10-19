package com.yieldnull.flappybird.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.yieldnull.flappybird.render.Renderer;

/**
 * Created by yieldnull on 10/18/16.
 */
public class StartScreen extends ScreenBase {

    public StartScreen(Renderer renderer) {
        super(renderer);
    }

    @Override
    public void show() {
        renderer.batchStart();
        renderer.scrollLand();
        renderer.hoverBird();
        renderer.batchEnd();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        show();
    }
}
