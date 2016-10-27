package com.yieldnull.flappybird.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    public static Sprite textOver;

    public static Sprite tutorial;

    public static Array<Sprite> scoreFont;


    public static Sprite buttonPlay;
    public static Sprite buttonScore;

    public static Sprite scorePanel;


    public static Sprite black;
    public static Sprite white;

    public static Sprite title;


    public static Array<Sprite> medals;
    public static Array<Sprite> scorePanelNums;

    public static Sound soundDie;
    public static Sound soundHit;
    public static Sound soundPoint;
    public static Sound soundWing;
    public static Sound soundSwooshing;

    public static void dispose() {
        atlas.dispose();
        atlas = null;

        soundDie.dispose();
        soundHit.dispose();
        soundPoint.dispose();
        soundSwooshing.dispose();
        soundWing.dispose();
    }


    public static void load() {
        atlas = new TextureAtlas("gfx/atlas.atlas");

        land = atlas.createSprite("land");
        background = atlas.createSprite("bg_day");
        bird = atlas.createSprites("bird1");

        pipeUp = atlas.createSprite("pipe_up");
        pipeDown = atlas.createSprite("pipe_down");

        textReady = atlas.createSprite("text_ready");
        textOver = atlas.createSprite("text_game_over");

        tutorial = atlas.createSprite("tutorial");

        scoreFont = atlas.createSprites("font");

        buttonPlay = atlas.createSprite("button_play");
        buttonScore = atlas.createSprite("button_score");

        scorePanel = atlas.createSprite("score_panel");

        black = atlas.createSprite("black");
        white = atlas.createSprite("white");

        title = atlas.createSprite("title");


        medals = atlas.createSprites("medals");
        scorePanelNums = atlas.createSprites("number_score");

        soundDie = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_die.ogg"));
        soundHit = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_hit.ogg"));
        soundPoint = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_point.ogg"));
        soundSwooshing = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_swooshing.ogg"));
        soundWing = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_wing.ogg"));
    }
}
