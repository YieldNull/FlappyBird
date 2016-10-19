package com.yieldnull.flappybird.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.yieldnull.flappybird.render.Renderer;

/**
 * Created by yieldnull on 10/18/16.
 */

public class ScreenBase extends ScreenAdapter {

    protected final Renderer renderer;

    public ScreenBase(Renderer renderer) {
        this.renderer = renderer;
    }
}
