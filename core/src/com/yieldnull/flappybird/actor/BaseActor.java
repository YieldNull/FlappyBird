package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Base Actor for fade in/out.
 * <p>
 * Created by yieldnull on 10/23/16.
 */

public abstract class BaseActor extends Actor {

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        Color batchColor = batch.getColor();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        draw(batch);
        batch.setColor(batchColor);

    }

    public abstract void draw(Batch batch);
}
