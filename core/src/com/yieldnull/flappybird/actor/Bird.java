package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

import aurelienribon.bodyeditor.BodyEditorLoader;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Bird extends Actor {

    private static final float HOVER_HEIGHT = 3.2f;
    private static final float HOVER_STEP = 0.35f;
    private static final float FLY_SPEED = 1 / 7f;


    private boolean isFlying;
    private boolean isDropped;
    private boolean isHoveringUp;

    private float flyStateTime;
    private Body body;

    private float rotation;

    private Animation birdAnimation;
    private float birdY = Coordinate.bird.y;

    public Bird(World world) {
        birdAnimation = new Animation(FLY_SPEED, Assets.bird);
        setOrigin(21.5f, 24.5f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(Coordinate.mapSceneToWorld(new Vector2(Coordinate.bird.x + getOriginX(),
                Coordinate.bird.y + getOriginY())));

        body = world.createBody(bodyDef);
        body.setUserData(Bird.class);


        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("gfx/birdVertex.json"));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = true;

        loader.attachFixture(body, "bird0_1", fixtureDef, 1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        flyStateTime += Gdx.graphics.getDeltaTime();

        if (isFlying) {
            renderFlying(batch);
        } else if (isDropped) {
            renderDropped(batch);
        } else {
            renderHovering(batch);
        }
    }

    public void jumpUp() {
        if (!isFlying) {
            isFlying = true;
            birdAnimation.setFrameDuration(FLY_SPEED / 2);
        }

        if (Coordinate.mapWorldToScene(body.getPosition()).y < Assets.background.getHeight()) {
            body.setLinearVelocity(0, 7f);
        }
    }

    public void hitLand() {
        isDropped = true;
        isFlying = false;
    }

    public void hitPipe() {

    }

    public void destroyBody(World world) {
        world.destroyBody(body);
    }


    private void renderHovering(Batch batch) {
        if (!isHoveringUp && birdY >= Coordinate.bird.y - HOVER_HEIGHT) {
            birdY -= HOVER_STEP;
        } else {
            isHoveringUp = true;
            if (birdY <= Coordinate.bird.y + HOVER_HEIGHT) {
                birdY += HOVER_STEP;
            } else {
                isHoveringUp = false;
            }
        }

        TextureRegion bird = birdAnimation.getKeyFrame(flyStateTime, true);
        batch.draw(birdAnimation.getKeyFrame(flyStateTime, true),
                Coordinate.bird.x, birdY,
                getOriginX(), getOriginY(),
                bird.getRegionWidth(), bird.getRegionHeight(),
                1, 1,
                rotation);
    }

    private void renderFlying(Batch batch) {
        Vector2 pos = Coordinate.mapWorldToScene(body.getPosition());
        birdY = pos.y;


        Vector2 bodyPos = body.getPosition();

        if (body.getLinearVelocity().y < -5f) {
            rotation -= 6f;
        } else {
            rotation += 10f;
        }
        rotation = MathUtils.clamp(rotation, -90, 20);


        body.setTransform(bodyPos, rotation * MathUtils.degreesToRadians);

        TextureRegion bird = birdAnimation.getKeyFrame(flyStateTime, true);
        batch.draw(bird, pos.x - getOriginX(), pos.y - getOriginY(),
                getOriginX(), getOriginY(),
                bird.getRegionWidth(), bird.getRegionHeight(),
                1, 1,
                rotation);

    }

    private void renderDropped(Batch batch) {
        TextureRegion bird = Assets.bird.get(1);

        Vector2 pos = Coordinate.mapWorldToScene(body.getPosition());

        batch.draw(bird,
                pos.x - getOriginX(),
                Assets.land.getHeight() - getOriginX() / 2f,
                getOriginX(), getOriginY(),
                bird.getRegionWidth(), bird.getRegionHeight(),
                1, 1,
                -90f);
    }

}
