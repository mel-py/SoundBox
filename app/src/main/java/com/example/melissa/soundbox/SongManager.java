package com.example.melissa.soundbox;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by Melissa on 2018-04-27.
 */

public class SongManager {
    private String mp3pattern = ".mp3";
    private ArrayList<song> songList = new ArrayList<>(); //stores all the songs with data
    private FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();

    public ArrayList<song> getFiles(String PATH) {
        if (PATH != null) {
            File home = new File(PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        songList.add(getSongInfo(file));
                    }
                }
            } else if (listFiles != null && listFiles.length == 0) {
                System.out.println("No files found");
            } else if (listFiles == null) {
                System.out.println("File directory is null");
            }
        }
        return songList;
    }

    private void scanDirectory (File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        songList.add(getSongInfo(file));
                    }
                }
            }
        }
    }

    //get each song in the directory as well as its metadata
    public song getSongInfo (File song) {
        song currentSong;
        String name, artist, album;
        byte [] art;

        try {
            mmr.setDataSource(song.getPath());
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        if (mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE) != null) {
            name = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
        } else {
            name = song.getName().substring(0, (song.getName().length() - 4));
        }

        if (mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST) != null) {
            artist = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        } else {
            artist = "Unknown";
        }

        if (mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM) != null) {
            album = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        } else {
            album = "Unknown";
        }

        currentSong = new song(song.getPath(), name, artist, album);

        return currentSong;
    }

    //Because app takes to long to load when loading all album art right away
    //Reduce load time by only calling it when needed
    public byte[] getAlbumArt(String path) {
        mmr.setDataSource(path);

        return mmr.getEmbeddedPicture();
    }

    public void destory() {
        if (mmr != null) {
            mmr.release();
        }
    }
}
