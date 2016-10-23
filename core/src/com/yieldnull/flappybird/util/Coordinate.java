package com.yieldnull.flappybird.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Coordinate {

    public static final Vector2 bird = new Vector2(
            ((Assets.background.getWidth() - Assets.bird.get(0).getWidth()) / 4 + 10),
            (Assets.background.getHeight() / 2) - 30);

    public static final Vector2 tutorial = new Vector2(
            (Assets.background.getWidth() - Assets.tutorial.getWidth()) / 2,
            bird.y + Assets.bird.get(0).getHeight()
                    - Assets.tutorial.getHeight() + 16);

    public static final Vector2 textReady = new Vector2(
            (Assets.background.getWidth() - Assets.textReady.getWidth()) / 2,
            tutorial.y + Assets.textReady.getHeight() + 50);

    public static final Vector2 textScore = new Vector2(
            Assets.background.getWidth() / 2,
            textReady.y + Assets.scoreFont.get(0).getHeight() + 50);


    public static final Vector2 buttonPlay = new Vector2(
            (Assets.background.getWidth() - Assets.buttonScore.getWidth() - Assets.buttonPlay.getWidth()) / 3,
            Assets.land.getHeight() - 10f);

    public static final Vector2 buttonScore = new Vector2(
            buttonPlay.x * 2 + Assets.buttonPlay.getWidth(),
            buttonPlay.y);

    public static final Vector2 scorePanel = new Vector2(
            (Assets.background.getWidth() - Assets.scorePanel.getWidth()) / 2,
            (Assets.background.getHeight() - Assets.scorePanel.getHeight()) / 2);


    public static final Vector2 textOver = new Vector2(
            (Assets.background.getWidth() - Assets.textOver.getWidth()) / 2,
            scorePanel.y + Assets.scorePanel.getHeight()
                    + scorePanel.y - (buttonPlay.y + Assets.buttonPlay.getHeight()));


    public static final Vector2 birdCenter = new Vector2(
            ((Assets.background.getWidth() - Assets.bird.get(0).getWidth()) / 2),
            (Assets.background.getHeight() - Assets.bird.get(0).getHeight()) / 2);


    public static final Vector2 title = new Vector2(
            (Assets.background.getWidth() - Assets.title.getWidth()) / 2,
            birdCenter.y + Assets.bird.get(0).getHeight()
                    + birdCenter.y - (buttonPlay.y + Assets.buttonPlay.getHeight()));


    public static final Vector2 medal = new Vector2(scorePanel.x + 32, scorePanel.y + 38);


    public static final Vector2 scoreSum = new Vector2(scorePanel.x + 209, scorePanel.y + 71);

    public static final Vector2 scoreMax = new Vector2(scorePanel.x + 209, scorePanel.y + 29);

    /**
     * when shape is set as box, the origin pointer is in the center
     *
     * @param scenePos
     * @param size
     * @return
     */
    public static Vector2 mapSceneToWorld(Vector2 scenePos, Vector2 size) {
        float x, y;

        x = scenePos.x - Assets.background.getWidth() / 2 + size.x / 2;
        y = scenePos.y - Assets.background.getHeight() / 2 + size.y / 2;

        return new Vector2(x / Constants.BOX2D_WORLD_RATIO, y / Constants.BOX2D_WORLD_RATIO);
    }

    public static Vector2 mapSceneToWorld(Vector2 scenePos) {
        float x, y;

        x = scenePos.x - Assets.background.getWidth() / 2;
        y = scenePos.y - Assets.background.getHeight() / 2;

        return new Vector2(x / Constants.BOX2D_WORLD_RATIO, y / Constants.BOX2D_WORLD_RATIO);
    }


    public static Vector2 mapWorldToScene(Vector2 worldPos, Vector2 size) {
        float x, y;

        x = worldPos.x * Constants.BOX2D_WORLD_RATIO + Assets.background.getWidth() / 2 - size.x / 2;
        y = worldPos.y * Constants.BOX2D_WORLD_RATIO + Assets.background.getHeight() / 2 - size.y / 2;

        return new Vector2(x, y);
    }

    public static Vector2 mapWorldToScene(Vector2 worldPos) {
        float x, y;

        x = worldPos.x * Constants.BOX2D_WORLD_RATIO + Assets.background.getWidth() / 2;
        y = worldPos.y * Constants.BOX2D_WORLD_RATIO + Assets.background.getHeight() / 2;

        return new Vector2(x, y);
    }


    public static void sceneToWorld(Vector2[] vertices) {
        for (Vector2 v : vertices) {
            v.x /= (float) Constants.BOX2D_WORLD_RATIO;
            v.y /= (float) Constants.BOX2D_WORLD_RATIO;
        }
    }
}
