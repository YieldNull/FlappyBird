package com.yieldnull.flappybird.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yieldnull.flappybird.actor.BaseActor;
import com.yieldnull.flappybird.actor.Bird;
import com.yieldnull.flappybird.actor.Land;
import com.yieldnull.flappybird.actor.Launcher;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

/**
 * Created by yieldnull on 10/23/16.
 */

public class LauncherScreen extends ScreenAdapter {

    private Stage stage;

    public LauncherScreen(Game game) {

        stage = new Stage(new StretchViewport(Assets.background.getWidth(), Assets.background.getHeight()));
        Gdx.input.setInputProcessor(stage);


        stage.addActor(new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.background, 0, 0);
            }
        });
        stage.addActor(new Land());
        stage.addActor(new Bird(Coordinate.birdCenter));
        stage.addActor(new Launcher(new LauncherClickedListener(game, stage)));
        stage.addActor(new BaseActor() {
            @Override
            public void draw(Batch batch) {
                batch.draw(Assets.title, Coordinate.title.x, Coordinate.title.y);
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

        stage.act(delta);
        stage.draw();
    }


    @Override
    public void hide() {
        stage.dispose();
    }
}
