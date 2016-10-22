package com.yieldnull.flappybird.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by yieldnull on 10/19/16.
 */

public class Coordinate {

    public static final Vector2 bird = new Vector2(
            (int) ((Assets.background.getWidth() - Assets.bird.get(0).getWidth()) / 4 + 10),
            (int) (Assets.background.getHeight() / 2) - 30);

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
