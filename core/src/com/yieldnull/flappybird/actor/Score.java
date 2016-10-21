package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Score extends Actor{
    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(Assets.scoreFont.get(0), Coordinate.textScore.x,Coordinate.textScore.y);
    }
}
