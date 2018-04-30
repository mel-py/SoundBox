package com.example.melissa.soundbox;

import java.io.Serializable;

/**
 * Created by Melissa on 2018-04-27.
 */

public class song implements Serializable {
    private String songPath, songName, songArtist, songAlbum;

    public song (String path, String name, String artist, String album) {
        songPath = path;
        songName = name;
        songArtist = artist;
        songAlbum = album;
    }

    public String getPath() {
        return songPath;
    }

    public String getName() {
        return songName;
    }

    public String getArtist() {
        return songArtist;
    }

    public String getAlbum() {
        return songAlbum;
    }
}
