package com.yieldnull.flappybird.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.yieldnull.flappybird.actor.BaseActor;
import com.yieldnull.flappybird.actor.Launcher;
import com.yieldnull.flappybird.util.Assets;

/**
 * Created by yieldnull on 10/23/16.
 */

public class LauncherClickedListener implements Launcher.ButtonOnClickedListener {
    private Game game;
    private Stage stage;

    private boolean clicked;

    public LauncherClickedListener(Game game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }

    @Override
    public void onStartButtonClicked() {

        if (!clicked) {
            clicked = true;

            BaseActor blackScreen = new BaseActor() {
                @Override
                public void draw(Batch batch) {
                    batch.draw(Assets.black, 0, 0, Assets.background.getWidth(), Assets.background.getHeight());
                }
            };

            stage.addActor(blackScreen);

            blackScreen.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new GameScreen(game));
                }
            })));

            Assets.soundSwooshing.play();
        }
    }

    @Override
    public void onScoreButtonClicked() {
        onStartButtonClicked();
    }
}
