package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Score extends Actor {

    private int score;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int num = score;
        int digit;
        float totalWidth = 0;

        Stack<TextureRegion> regions = new Stack<TextureRegion>();
        do {
            digit = num % 10;

            TextureRegion region = Assets.scoreFont.get(digit);
            regions.push(region);
            totalWidth += region.getRegionWidth();

            num /= 10;
        } while (num > 0);

        float preWidths = 0;
        float startX = Coordinate.textScore.x - totalWidth / 2f;
        while (!regions.empty()) {
            TextureRegion region = regions.pop();
            batch.draw(region, startX + preWidths, Coordinate.textScore.y);
            preWidths += region.getRegionWidth();
        }
    }

    public void add() {
        score++;
    }
}
