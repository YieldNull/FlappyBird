package com.yieldnull.flappybird.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yieldnull.flappybird.actor.Bird;
import com.yieldnull.flappybird.actor.Pipes;
import com.yieldnull.flappybird.actor.Scene;
import com.yieldnull.flappybird.actor.Score;
import com.yieldnull.flappybird.actor.Tutorial;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Constants;

/**
 * Created by yieldnull on 10/18/16.
 */
public class GameScreen extends ScreenAdapter implements Pipes.BirdThroughListener {

    private final Stage stage;
    private final World world;

    private boolean isStarted;
    private boolean isGameOver;
    private boolean isBirdDropped;

    private static final float STEP_TIME = 1f / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private float accumulator = 0;


    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
    private OrthographicCamera camera;

    private final Score score;
    private final Pipes pipes;

    public GameScreen() {
        Box2D.init();
        world = new World(new Vector2(0, -20f), true);
        stage = new Stage(new StretchViewport(Assets.background.getWidth(), Assets.background.getHeight()));


        camera = new OrthographicCamera(stage.getViewport().getWorldWidth() / Constants.BOX2D_WORLD_RATIO,
                stage.getViewport().getWorldHeight() / Constants.BOX2D_WORLD_RATIO);

        final Scene scene = new Scene(world);
        pipes = new Pipes((int) stage.getViewport().getWorldWidth(),
                (int) (stage.getViewport().getWorldHeight() - Assets.land.getHeight()),
                world,this);
        final Bird bird = new Bird(world);
        final Tutorial tutorial = new Tutorial();
        score = new Score();

        stage.addActor(scene);
        stage.addActor(pipes);
        stage.addActor(bird);
        stage.addActor(tutorial);
        stage.addActor(score);

        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isStarted) {
                    isStarted = true;
                    bird.jumpUp();
                    pipes.startMoving();
                    tutorial.addAction(Actions.sequence(Actions.fadeOut(0.5f, Interpolation.pow2In), Actions.hide()));
                } else if (!isGameOver) {
                    bird.jumpUp();
                }
                return false;
            }
        });


        world.setContactListener(new ContactListener() {
            private boolean hitLand(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();

                return userDataA.equals(Scene.class) || userDataB.equals(Scene.class);
            }

            @Override
            public void beginContact(Contact contact) {
                gameOver();

                scene.stopMoving();
                pipes.stopMoving();


                if (hitLand(contact)) {
                    bird.hitLand();
                    isBirdDropped = true;
                } else {
                    bird.hitPipe();
                }

            }

            @Override
            public void endContact(Contact contact) {
                if (hitLand(contact)) {
                    bird.destroyBody(world);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    @Override
    public void show() {
        stage.act();
        stage.draw();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (isStarted && !isBirdDropped) {
            stepWorld(delta);
        }

        stage.act(delta);
        stage.draw();

        stage.getCamera().update();
        renderer.render(world, camera.combined);
    }

    @Override
    public void birdThrough() {
        score.add();
    }

    @Override
    public void hide() {
        stage.dispose();
        world.dispose();
    }

    private void stepWorld(float delta) {

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }


    private void gameOver() {
        isGameOver = true;
    }


}
