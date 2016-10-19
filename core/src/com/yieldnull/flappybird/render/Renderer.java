package com.yieldnull.flappybird.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by yieldnull on 10/18/16.
 */

public class Renderer {

    private final World world;
    private final SpriteBatch batch;

    private static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private static final int LAND_HEIGHT = SCREEN_HEIGHT / 5;
    private static final int LAND_MOVING_SPEED = SCREEN_WIDTH / 3;

    private int leftLandWidth = SCREEN_WIDTH;
    private Animation birdAnimation;
    private float birdTime;
    private int hover;


    public Renderer() {
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        batch = new SpriteBatch();

        birdAnimation = new Animation(1 / 7f, Assets.bird);
    }

    public void batchStart() {
        batch.begin();
    }

    public void batchEnd() {
        batch.end();
    }

    public void scrollLand() {
        leftLandWidth -= 0.025 * LAND_MOVING_SPEED;

        if (leftLandWidth <= 0) {
            leftLandWidth += Gdx.graphics.getWidth();
        }

        int width = (int) (Assets.land.getWidth() * leftLandWidth / SCREEN_WIDTH);

        TextureRegion leftLand = new TextureRegion(Assets.land,
                (int) (Assets.land.getWidth() - width), 0,
                width, (int) Assets.land.getHeight());

        TextureRegion rightLand = new TextureRegion(Assets.land, 0, 0,
                (int) (Assets.land.getWidth() - width), (int) Assets.land.getHeight());

        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(leftLand, 0, 0, leftLandWidth, LAND_HEIGHT);
        batch.draw(rightLand, leftLandWidth, 0, Gdx.graphics.getWidth() - leftLandWidth, LAND_HEIGHT);
    }

    private int birdHeight = SCREEN_HEIGHT / 2;
    private boolean birdUp;

    public void hoverBird() {
        birdTime += Gdx.graphics.getDeltaTime();

        if (++hover % 2 == 0) {
            if (!birdUp && birdHeight >= SCREEN_HEIGHT / 2 - 10) {
                birdHeight -= 2;
            } else {
                birdUp = true;
                if (birdHeight <= SCREEN_HEIGHT / 2 + 10) {
                    birdHeight += 2;
                } else {
                    birdUp = false;
                }
            }
        }

        batch.draw(birdAnimation.getKeyFrame(birdTime, true),
                SCREEN_WIDTH / 2.4f,
                birdHeight,
                SCREEN_WIDTH / 6, SCREEN_WIDTH / 6);

    }

    public void dispose() {
        world.dispose();
        batch.dispose();
        Assets.dispose();
    }


    private static class Assets {
        static final TextureAtlas atlas = new TextureAtlas("gfx/atlas.txt");

        static final Sprite land = atlas.createSprite("land");
        static final Sprite background = atlas.createSprite("bg_day");
        static final Array<Sprite> bird = atlas.createSprites("bird1");

        static void dispose() {
            atlas.dispose();
        }
    }

}
