package com.yieldnull.flappybird.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yieldnull.flappybird.actor.BaseActor;
import com.yieldnull.flappybird.actor.Bird;
import com.yieldnull.flappybird.actor.Land;
import com.yieldnull.flappybird.actor.Pipes;
import com.yieldnull.flappybird.actor.Score;
import com.yieldnull.flappybird.actor.Launcher;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Constants;
import com.yieldnull.flappybird.util.Coordinate;
import com.yieldnull.flappybird.util.History;

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

    private Game game;


    public GameScreen(Game game) {
        this.game = game;

        Box2D.init();
        world = new World(new Vector2(0, -20f), true);

        stage = new Stage(new StretchViewport(Assets.background.getWidth(), Assets.background.getHeight()));
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera(stage.getViewport().getWorldWidth() / Constants.BOX2D_WORLD_RATIO,
                stage.getViewport().getWorldHeight() / Constants.BOX2D_WORLD_RATIO);


        score = new Score(Assets.scoreFont, Coordinate.textScore, true);
        pipes = new Pipes((int) stage.getViewport().getWorldWidth(),
                (int) (stage.getViewport().getWorldHeight() - Assets.land.getHeight()),
                world, this);
        final Bird bird = new Bird(Coordinate.bird, world);
        final Land land = new Land(world);


        final Actor tutorial = new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.textReady, Coordinate.textReady.x, Coordinate.textReady.y);
                batch.draw(Assets.tutorial, Coordinate.tutorial.x, Coordinate.tutorial.y);
            }
        };

        BaseActor blackScreen = new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.black, 0, 0, Assets.background.getWidth(), Assets.background.getHeight());
            }
        };

        BaseActor background = new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.background, 0, 0);
            }
        };

        stage.addActor(background);
        stage.addActor(pipes);
        stage.addActor(bird);
        stage.addActor(land);
        stage.addActor(tutorial);
        stage.addActor(score);
        stage.addActor(blackScreen);

        blackScreen.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.hide()));

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

                return userDataA.equals(Land.class) || userDataB.equals(Land.class);
            }

            @Override
            public void beginContact(Contact contact) {
                if (!isGameOver) {
                    gameOver();
                }

                land.stopMoving();
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

//        stage.getCamera().update();
//        renderer.render(world, camera.combined);
    }


    @Override
    public void hide() {
        stage.dispose();
        world.dispose();
    }


    @Override
    public void birdThrough() {
        score.add();

        Assets.soundPoint.play();
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

        Actor flash = new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.white, 0, 0, Assets.background.getWidth(), Assets.background.getHeight());
            }
        };
        stage.addActor(flash);


        Actor textOver = new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.textOver, Coordinate.textOver.x, Coordinate.textOver.y);
            }
        };


        final Score currentScore = new Score(Assets.scoreNumbers, Coordinate.scoreSum, false);
        Score maxScore = new Score(Assets.scoreNumbers, Coordinate.scoreMax, false);

        int max = History.getMax();
        maxScore.setScore(max >= 0 ? max : score.getScore());

        Actor scorePanel = new BaseActor() {

            private int rank=History.rank(score.getScore());

            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.scorePanel, Coordinate.scorePanel.x, Coordinate.scorePanel.y);

                Sprite sprite = Assets.medals.get(rank);
                batch.draw(sprite, Coordinate.medal.x, Coordinate.medal.y);

            }
        };

        final Group group = new Group();
        group.setVisible(false);
        group.addActor(textOver);
        group.addActor(scorePanel);
        group.addActor(maxScore);
        group.addActor(new Launcher(new LauncherClickedListener(game, stage)));


        flash.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.hide(),
                Actions.delay(1f, Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        score.addAction(Actions.fadeOut(0.2f));

                        stage.addActor(group);
                        group.addAction(Actions.sequence(Actions.alpha(0), Actions.show(), Actions.fadeIn(0.5f),
                                Actions.run(new Runnable() {

                                    @Override
                                    public void run() {
                                        group.addActor(currentScore);
                                        currentScore.addAction(Actions.repeat(score.getScore(), Actions.delay(0.05f, Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                currentScore.add();
                                            }
                                        }))));
                                    }
                                })));

                        Assets.soundSwooshing.play();
                    }
                }))));
    }
}
