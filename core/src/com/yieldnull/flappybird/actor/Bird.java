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
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

import aurelienribon.bodyeditor.BodyEditorLoader;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Bird extends BaseActor {

    private static final float HOVER_HEIGHT = 3.2f;
    private static final float HOVER_STEP = 0.35f;
    private static final float FLY_SPEED = 1 / 7f;


    private static final float ORIGIN_X = 21.5f;
    private static final float ORIGIN_Y = 24.5f;

    private boolean isFlying;
    private boolean isDropped;
    private boolean isHoveringUp;
    private boolean isSoundPlayed;


    private float rotation;
    private float flyStateTime;

    private Body body;
    private Animation birdAnimation;

    private Vector2 initPos;
    private float posY;

    /**
     * Draw a bird at position.
     *
     * @param position origin position
     */
    public Bird(Vector2 position) {
        setOrigin(ORIGIN_X, ORIGIN_Y);

        posY = position.y;
        initPos = position;

        birdAnimation = new Animation(FLY_SPEED, Assets.bird);
    }

    /**
     * Draw a bird at position, and create a corresponding body in Box2d World.
     *
     * @param position origin position
     * @param world    Box2d World
     */
    public Bird(Vector2 position, World world) {
        this(position);


        // create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(Coordinate.mapSceneToWorld(new Vector2(initPos.x + getOriginX(),
                initPos.y + getOriginY())));

        body = world.createBody(bodyDef);


        // set user data
        body.setUserData(Bird.class);


        // load vertices
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("gfx/birdVertex.json"));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = true;

        loader.attachFixture(body, "bird0_1", fixtureDef, 1f);
    }

    @Override
    public void draw(Batch batch) {
        flyStateTime += Gdx.graphics.getDeltaTime();

        if (isFlying) {
            renderFlying(batch);
        } else if (isDropped) {
            renderDropped(batch);
        } else {
            renderHovering(batch);
        }
    }

    /**
     * Jump up when user touch screen
     */
    public void jumpUp() {

        // first touch
        if (!isFlying) {
            isFlying = true;
            birdAnimation.setFrameDuration(FLY_SPEED / 2);
        }

        // do not jump up when the bird is invisible
        if (Coordinate.mapWorldToScene(body.getPosition()).y < Assets.background.getHeight()) {
            body.setLinearVelocity(0, 7f);

            Assets.soundWing.play();
        }
    }

    /**
     * bird dropped
     */
    public void hitLand() {
        isDropped = true;
        isFlying = false;

        if (!isSoundPlayed) {
            isSoundPlayed = true;
            Assets.soundHit.play();
        }
    }


    /**
     * bird hit a pip
     */
    public void hitPipe() {
        if (!isSoundPlayed) {
            isSoundPlayed = true;

            Assets.soundDie.play();
            Assets.soundHit.play();
        }
    }

    /**
     * destroy the Box2d Body
     *
     * @param world Box2d World
     */
    public void destroyBody(World world) {
        world.destroyBody(body);
    }


    /**
     * bird is hovering
     *
     * @param batch
     */
    private void renderHovering(Batch batch) {
        if (!isHoveringUp && posY >= initPos.y - HOVER_HEIGHT) {
            posY -= HOVER_STEP;
        } else {
            isHoveringUp = true;
            if (posY <= initPos.y + HOVER_HEIGHT) {
                posY += HOVER_STEP;
            } else {
                isHoveringUp = false;
            }
        }

        TextureRegion bird = birdAnimation.getKeyFrame(flyStateTime, true);
        batch.draw(birdAnimation.getKeyFrame(flyStateTime, true),
                initPos.x, posY,
                getOriginX(), getOriginY(),
                bird.getRegionWidth(), bird.getRegionHeight(),
                1, 1,
                rotation);
    }

    /**
     * bird is flying
     *
     * @param batch
     */
    private void renderFlying(Batch batch) {
        Vector2 pos = Coordinate.mapWorldToScene(body.getPosition());
        Vector2 bodyPos = body.getPosition();

        posY = pos.y;

        // rotation
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

    /**
     * bird has dropped. set rotation to -90°
     *
     * @param batch
     */
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
