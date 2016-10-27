package com.yieldnull.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.yieldnull.flappybird.util.Assets;
import com.yieldnull.flappybird.util.Coordinate;

/**
 * Launcher with two button.
 * <p>
 * Created by yieldnull on 10/23/16.
 */

public class Launcher extends Group {

    public interface ButtonOnClickedListener {
        void onStartButtonClicked();

        void onScoreButtonClicked();
    }

    public Launcher(final ButtonOnClickedListener buttonOnClickedListener) {
        StartButton startButton = new StartButton();
        ScoreButton scoreButton = new ScoreButton();

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonOnClickedListener.onStartButtonClicked();
            }
        });

        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonOnClickedListener.onScoreButtonClicked();
            }
        });

        addActor(startButton);
        addActor(scoreButton);
    }


    private static class StartButton extends BaseActor {

        StartButton() {
            setBounds(Coordinate.buttonPlay.x, Coordinate.buttonPlay.y,
                    Assets.buttonPlay.getWidth(), Assets.buttonPlay.getHeight());
        }

        @Override
        public void draw(Batch batch) {
            batch.draw(Assets.buttonPlay, Coordinate.buttonPlay.x, Coordinate.buttonPlay.y);
        }
    }

    private static class ScoreButton extends BaseActor {

        ScoreButton() {
            setBounds(Coordinate.buttonScore.x, Coordinate.buttonScore.y,
                    Assets.buttonScore.getWidth(), Assets.buttonScore.getHeight());
        }

        @Override
        public void draw(Batch batch) {
            batch.draw(Assets.buttonScore, Coordinate.buttonScore.x, Coordinate.buttonScore.y);
        }
    }
}
