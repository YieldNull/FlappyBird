package com.yieldnull.flappybird.util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by yieldnull on 10/19/16.
 */
public class Assets {
    private static TextureAtlas atlas;

    public static Sprite land;
    public static Sprite background;
    public static Array<Sprite> bird;

    public static Sprite pipeUp;
    public static Sprite pipeDown;


    public static Sprite textReady;
    public static Sprite tutorial;

    public static Array<Sprite> scoreFont;


    public static void dispose() {
        atlas.dispose();
        atlas = null;
    }

    public static void load() {
        atlas = new TextureAtlas("gfx/atlas.atlas");
        land = atlas.createSprite("land");
        background = atlas.createSprite("bg_day");
        bird = atlas.createSprites("bird1");

        pipeUp = atlas.createSprite("pipe_up");
        pipeDown = atlas.createSprite("pipe_down");

        textReady = atlas.createSprite("text_ready");
        tutorial = atlas.createSprite("tutorial");

        scoreFont = atlas.createSprites("font");
    }
}
