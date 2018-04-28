package com.example.melissa.soundbox;

import java.io.Serializable;

/**
 * Created by Melissa on 2018-04-27.
 */

public class song implements Serializable {
    private String songPath, songName, songArtist, songAlbum;
    private byte [] songAlbumArt;
    private boolean isFav;

    public song (String path, String name, String artist, String album, byte [] albumArt) {
        songPath = path;
        songName = name;
        songArtist = artist;
        songAlbum = album;
        songAlbumArt = albumArt;
        isFav = false;
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

    public byte [] getAlbumArt() {
        return songAlbumArt;
    }
}
