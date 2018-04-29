package com.example.melissa.soundbox;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
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

    private final String PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/");

    public PlaylistManager(Context ctx) {
        Toast.makeText(ctx, PATH, Toast.LENGTH_LONG).show();
        utility_manager = new utils();

        playlists = new HashMap<>();
        manager = new SongManager();
        ArrayList<song> songList = manager.getFiles(PATH);
        playlists.put("allTracks", songList);

        int flag = loadPlaylist(ctx);
        if (flag != 0){
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

    public void savePlaylists(Context ctx) {
        try {
            ArrayList<song> favouritesList = playlists("favourites");
            FileOutputStream fos = ctx.openFileOutput("favourites.sav", Context.MODE_PRIVATE);
            for (int i = 0; i < favouritesList.size(); i++) {
                song curSong = favouritesList.get(i);
                fos.write((curSong.getPath() + "|" + curSong.getName() + "|"
                        + curSong.getArtist() + "|" + curSong.getAlbum() + "|" +
                        curSong.getAlbumArt() + "\n").getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int loadPlaylist(Context ctx) {
        try {
            FileInputStream fis = ctx.openFileInput("favourites.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line = in.readLine();
            ArrayList<song> favouritesList = new ArrayList<>();
            while (line != null) {
                String[] parts = line.split("\\|");
                favouritesList.add(new song(parts[0], parts[1], parts[2], parts[3],
                        parts[4].getBytes()));
                line = in.readLine();
            }
            fis.close();
            playlists.put("favourites", favouritesList);
        } catch (FileNotFoundException e) {
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
