package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Constants;
import com.yieldnull.flappybird.util.Coordinate;

/**
 * Game Land
 * <p>
 * Created by yieldnull on 10/19/16.
 */

public class Land extends BaseActor {

    private int leftPartWidth;
    private boolean isMoving = true;


    /**
     * draw a land.
     */
    public Land() {
    }

    /**
     * draw a land with the corresponding Body in Box2d World
     *
     * @param world Box2d World
     */
    public Land(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(Coordinate.mapSceneToWorld(new Vector2(0, 0), new Vector2(Assets.land.getWidth(), Assets.land.getHeight())));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                Assets.land.getWidth() / (2 * Constants.BOX2D_WORLD_RATIO),
                Assets.land.getHeight() / (2 * Constants.BOX2D_WORLD_RATIO));

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 0f);

        body.setUserData(Land.class);
        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {

        if (isMoving) {
            leftPartWidth -= Constants.SCENE_MOVING_SPEED;

            if (leftPartWidth <= 0) {
                leftPartWidth += Constants.WORLD_WIDTH;
            }
        }

        TextureRegion leftPart = new TextureRegion(Assets.land,
                (int) (Assets.land.getWidth() - leftPartWidth), 0,
                leftPartWidth,
                (int) Assets.land.getHeight());

        TextureRegion rightPart = new TextureRegion(Assets.land,
                0, 0,
                (int) (Assets.land.getWidth() - leftPartWidth),
                (int) Assets.land.getHeight());

        batch.draw(leftPart, 0, 0);
        batch.draw(rightPart, leftPartWidth, 0);
    }

    public void stopMoving() {
        isMoving = false;
    }
}
