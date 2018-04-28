package com.example.melissa.soundbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Melissa on 2018-04-27.
 */

public class utils {
    public ArrayList<song> sortAlphabetically(ArrayList<song> songs) {
        Collections.sort(songs, new Comparator<song>() {
            @Override
            public int compare(song lhs, song rhs) {
                String song1 = lhs.getName().toLowerCase();
                String song2 = rhs.getName().toLowerCase();
                return song1.compareTo(song2);
            }
        });

        return songs;
    }
}
