package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.yieldnull.flappybird.actor.BaseActor;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Score extends BaseActor {

    private int score;

    private Array<Sprite> numberSet;
    private Vector2 position;

    private boolean isCenter;

    public Score(Array<Sprite> numberSet, Vector2 position, boolean isCenter) {
        this.numberSet = numberSet;
        this.position = position;
        this.isCenter = isCenter;
    }

    @Override
    public void draw(Batch batch) {
        int num = score;
        int digit;
        float totalWidth = 0;

        List<TextureRegion> regions = new ArrayList<TextureRegion>();
        do {
            digit = num % 10;

            TextureRegion region = numberSet.get(digit);
            regions.add(region);
            totalWidth += region.getRegionWidth();

            num /= 10;
        } while (num > 0);

        float preWidths = 0;
        if (isCenter) {
            float startX = position.x - totalWidth / 2f;
            for (int i = regions.size() - 1; i >= 0; i--) {
                TextureRegion region = regions.get(i);
                batch.draw(region, startX + preWidths, position.y);
                preWidths += region.getRegionWidth();
            }
        } else {
            for (TextureRegion region : regions) {
                batch.draw(region, position.x - region.getRegionWidth() - preWidths, position.y);
                preWidths += region.getRegionWidth();
            }
        }
    }

    public void add() {
        score++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
