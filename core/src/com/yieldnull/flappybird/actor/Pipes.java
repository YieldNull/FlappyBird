package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Constants;
import com.yieldnull.flappybird.util.Coordinate;

import java.util.Random;

/**
 * All the three pair of pipes in the world.
 * <p>
 * Created by yieldnull on 10/19/16.
 */
public class Pipes extends Group {
    private static final int PIP_WIDTH = (int) Assets.pipeUp.getWidth();
    private static final int PIP_HEIGHT = (int) Assets.pipeUp.getHeight();

    private static final int BOTTOM_Y = (int) Assets.land.getHeight();

    private static final int HEAD_HEIGHT = 24;
    private static final int BODY_WIDTH = 48;

    private static final int GAP_VERTICAL = 104;
    private static final int GAP_HORIZONTAL = 160;


    private final World world;
    private final Pipe[] pipes = new Pipe[3];

    private final int totalHeight;
    private boolean isMoving;


    /**
     * The bird has flied through a pip group.
     */
    public interface BirdThroughListener {
        void birdThrough();
    }

    private BirdThroughListener birdThroughListener;

    public Pipes(World world, BirdThroughListener birdThroughListener) {
        this.world = world;
        this.birdThroughListener = birdThroughListener;

        this.totalHeight = (int) (Constants.WORLD_HEIGHT - BOTTOM_Y);

        for (int i = 0; i < pipes.length; i++) {
            Pipe pipe = new Pipe(i, 2 * Constants.WORLD_WIDTH + (i + 1) * GAP_HORIZONTAL);
            pipes[i] = pipe;
            addActor(pipe);
        }
    }

    public void startMoving() {
        isMoving = true;
    }

    public void stopMoving() {
        isMoving = false;
    }


    /**
     * A pair of pipe.
     */
    private class Pipe extends BaseActor {

        private int heightBottom;
        private int heightTop;

        private Body bodyBottom;
        private Body bodyTop;

        private float x;

        private int index;

        private int widthInScreen;

        private final int maxHeight = (int) (PIP_HEIGHT / 1.3f);
        private final int minHeight = PIP_HEIGHT / 8;

        private boolean birdThrough;

        Pipe(int index, float x) {
            this.x = x;
            this.index = index;

            randomHeight();

            bodyBottom = createBody(new Vector2(x, BOTTOM_Y),
                    new Vector2(PIP_WIDTH, heightBottom), true);

            bodyTop = createBody(new Vector2(x, Constants.WORLD_HEIGHT - heightTop),
                    new Vector2(PIP_WIDTH, heightTop), false);
        }


        @Override
        public void draw(Batch batch) {
            if (isMoving) {
                x -= Constants.SCENE_MOVING_SPEED;

                if (!visible()) {
                    recycle();
                }

                bodyBottom.setTransform(Coordinate.mapSceneToWorld(
                        new Vector2(x, BOTTOM_Y)), 0);
                bodyTop.setTransform(Coordinate.mapSceneToWorld(
                        new Vector2(x, Constants.WORLD_HEIGHT - heightTop)), 0);

                if (!birdThrough && x < Coordinate.bird.x) {
                    birdThrough = true;
                    birdThroughListener.birdThrough();
                }
            }

            TextureRegion pipUp = new TextureRegion(Assets.pipeUp,
                    0, 0, PIP_WIDTH, heightBottom);

            TextureRegion pipDown = new TextureRegion(Assets.pipeDown,
                    0, PIP_HEIGHT - heightTop, PIP_WIDTH, heightTop);

            batch.draw(pipUp, x, BOTTOM_Y);
            batch.draw(pipDown, x, Constants.WORLD_HEIGHT - heightTop);
        }


        private boolean visible() {
            return x > -getWidthInScreen();
        }

        private void recycle() {
            birdThrough = false;

            x = pipes[(index + pipes.length - 1) % pipes.length].x + GAP_HORIZONTAL;
            randomHeight();

            createFixture(bodyBottom, heightBottom, true);
            createFixture(bodyTop, heightTop, false);
        }

        private void randomHeight() {
            heightBottom = new Random().nextInt(((maxHeight - minHeight) + 1)) + minHeight;
            heightTop = totalHeight - heightBottom - GAP_VERTICAL;
        }

        private int getWidthInScreen() {
            if (widthInScreen == 0) {
                widthInScreen = (int) getStage().getViewport().project(new Vector2(Assets.pipeDown.getWidth(), 0)).x;
            }
            return widthInScreen;
        }


        private Body createBody(Vector2 position, Vector2 size, boolean isUp) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(Coordinate.mapSceneToWorld(position, size));
            Body body = world.createBody(bodyDef);

            createFixture(body, (int) size.y, isUp);

            body.setUserData(Pipes.class);

            return body;
        }

        private void createFixture(Body body, int height, boolean isUp) {

            while (body.getFixtureList().size > 0) {
                body.destroyFixture(body.getFixtureList().first());
            }

            PolygonShape bottomShape = new PolygonShape();
            PolygonShape topShape = new PolygonShape();

            // 8 vertices
            Vector2[] bottomVertices = new Vector2[4];
            Vector2[] topVertices = new Vector2[4];

            if (isUp) {
                bottomVertices[0] = new Vector2((PIP_WIDTH - BODY_WIDTH) / 2f, 0);
                bottomVertices[1] = new Vector2(bottomVertices[0].x + BODY_WIDTH, 0);
                bottomVertices[2] = new Vector2(bottomVertices[1].x, height - HEAD_HEIGHT);
                bottomVertices[3] = new Vector2(bottomVertices[0].x, bottomVertices[2].y);

                topVertices[0] = new Vector2(0, height - HEAD_HEIGHT);
                topVertices[1] = new Vector2(PIP_WIDTH, height - HEAD_HEIGHT);
                topVertices[2] = new Vector2(PIP_WIDTH, height);
                topVertices[3] = new Vector2(0, height);


            } else {

                bottomVertices[0] = new Vector2(0, 0);
                bottomVertices[1] = new Vector2(PIP_WIDTH, 0);
                bottomVertices[2] = new Vector2(PIP_WIDTH, HEAD_HEIGHT);
                bottomVertices[3] = new Vector2(0, HEAD_HEIGHT);

                topVertices[0] = new Vector2((PIP_WIDTH - BODY_WIDTH) / 2f, HEAD_HEIGHT);
                topVertices[1] = new Vector2((PIP_WIDTH - BODY_WIDTH) / 2f + BODY_WIDTH, HEAD_HEIGHT);
                topVertices[2] = new Vector2((PIP_WIDTH - BODY_WIDTH) / 2f + BODY_WIDTH, height);
                topVertices[3] = new Vector2((PIP_WIDTH - BODY_WIDTH) / 2f, height);

            }


            sceneToWorld(bottomVertices);
            sceneToWorld(topVertices);

            bottomShape.set(bottomVertices);
            topShape.set(topVertices);


            body.createFixture(bottomShape, 0f);
            body.createFixture(topShape, 0f);

            bottomShape.dispose();
            topShape.dispose();
        }
    }

    private static void sceneToWorld(Vector2[] vertices) {
        for (Vector2 v : vertices) {
            v.x /= (float) Constants.BOX2D_WORLD_RATIO;
            v.y /= (float) Constants.BOX2D_WORLD_RATIO;
        }
    }
}
