package com.example.melissa.soundbox;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Melissa on 2018-04-27.
 */

public class PlaylistManager {
    private HashMap<String, ArrayList<song>> playlists;
    private SongManager manager;
    private utils utility_manager;

    private final String PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");

    public PlaylistManager() {
        utility_manager = new utils();

        playlists = new HashMap<String, ArrayList<song>>();
        manager = new SongManager();
        int flag = loadPlaylist();
        if (flag != 0){
            ArrayList<song> songList = manager.getFiles(PATH);
            playlists.put("allTracks", songList);
            playlists.put("favourites", new ArrayList<song>());
        }
    }

    public void newPlaylist(String name) {
        playlists.put(name, new ArrayList<song>());
    }

    public void deletePlaylist(String name) {
        playlists.remove(name);
    }

    public void addToPlaylist(String name, song s) {
        playlists.get(name).add(s);
    }

    public void removeFromPlaylist(String name, int index) {
        playlists.get(name).remove(index);
    }

    public song getFromPlaylist(String name, int index) {
        return playlists.get(name).get(index);
    }

    public int getPlaylistSize(String name) {
        return playlists.get(name).size();
    }

    public ArrayList<song> playlists(String name) {
        return playlists.get(name);
    }

    public void savePlaylists() {
        //todo: implement
    }

    public int loadPlaylist() {
        //todo: implement
        return 0;
    }
}
