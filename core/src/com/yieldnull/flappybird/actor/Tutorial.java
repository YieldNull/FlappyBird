package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Tutorial extends Actor {

    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color color = getColor();
        Color batchColor = batch.getColor();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(Assets.textReady, Coordinate.textReady.x, Coordinate.textReady.y);
        batch.draw(Assets.tutorial, Coordinate.tutorial.x, Coordinate.tutorial.y);

        batch.setColor(batchColor);
    }
}
