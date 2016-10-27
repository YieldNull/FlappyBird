package com.yieldnull.flappybird.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by yieldnull on 10/23/16.
 */

public class History {

    private static final String FILE_NAME = "scores";

    /**
     * Get rank in history and store it.
     *
     * @param score
     * @return
     */
    public static int rank(int score) {

        List<Integer> records = readRecords();
        records.add(score);
        Collections.sort(records, Collections.<Integer>reverseOrder());

        saveRecords(records.subList(0, Math.min(records.size(), 3)));
        return records.indexOf(score);
    }

    /**
     * Get the max score in history.
     *
     * @return
     */
    public static int getMax() {
        List<Integer> records = readRecords();
        Collections.sort(records, Collections.<Integer>reverseOrder());

        return records.size() > 0 ? readRecords().get(0) : -1;
    }

    private static List<Integer> readRecords() {
        FileHandle handler = Gdx.files.local(FILE_NAME);
        List<Integer> records = new ArrayList<Integer>();

        if (handler.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(handler.read()));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    records.add(Integer.valueOf(line));
                }

                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return records;
    }

    private static void saveRecords(List<Integer> records) {
        FileHandle handler = Gdx.files.local(FILE_NAME);

        handler.delete();

        for (int record : records) {
            handler.writeString(String.format("%d\n", record), true);
        }
    }
}
