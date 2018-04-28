package com.example.melissa.soundbox;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
public class MainActivity extends Activity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;

    private int songPosition; //current position in the song array
    private boolean isPaused = true;

    private PlaylistManager pm;

    public ArrayList<song> songList; //unsorted
    private String curPlaylist = "allTracks";

    private Button playButton;
    private Button prvButton;
    private Button nextButton;
    private Button favButton;

    private TextView songInfo;
    private ImageButton albumButton;

    private CustomListViewAdaptor customListView;
    private ListView listView;

    private SeekBar progressBar;

    private Button showTracks;
    private Button showFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get permissions
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "need permission to access files", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        } else {
            finishSetUp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finishSetUp();
            } else {
                Toast.makeText(this, "Permission to access files denied", Toast.LENGTH_LONG).show();
            }

            return;
        }
    }

    public void finishSetUp() {
        pm = new PlaylistManager();

        if (pm.getPlaylistSize("allTracks") > 0) {
            mediaPlayer = new MediaPlayer();

            updatePlayList();
        }
        playButton = (Button) findViewById(R.id.btnPlay);
        prvButton = (Button) findViewById(R.id.btnPrv);
        nextButton = (Button) findViewById(R.id.btnNext);

        favButton = (Button) findViewById(R.id.favBtn);

        progressBar = (SeekBar) findViewById(R.id.progressSeekBar);
        songInfo = (TextView)findViewById(R.id.songInfo);
        albumButton = (ImageButton)findViewById(R.id.albumArtButton);

        showTracks = (Button) findViewById(R.id.tracksBtn);
        showFavourites = (Button) findViewById(R.id.favPlaylistBtn);

        showTracks.setOnClickListener(this);
        showFavourites.setOnClickListener(this);

        playButton.setOnClickListener(this);
        prvButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        favButton.setOnClickListener(this);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                song cur = pm.getFromPlaylist(curPlaylist, songPosition);
                byte [] albumPicture = cur.getAlbumArt();
                if (albumPicture != null) {
                    try {
                        albumButton.setImageBitmap(BitmapFactory.decodeByteArray(albumPicture, 0, albumPicture.length));
                    } catch (Exception e) {
                        albumButton.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.default_album));
                    }
                } else {
                    albumButton.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.default_album));
                }
                String songInformation = cur.getName() + " - " + cur.getArtist();
                songInfo.setText(songInformation);
                progressBar.setProgress(0);
                progressBar.setMax(mp.getDuration());
                progressBar.postDelayed(onEverySecond, 1000);
            }
        });

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //update the progress bar as music is playing
    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
                progressBar.postDelayed(onEverySecond, 1000);
            }
        }
    };

    @Override
    public void onClick (View v) {
        switch(v.getId()) {
            case R.id.btnPlay:
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    if (isPaused == true) {
                        unPause();
                    } else {
                        playMusic();
                    }
                }
                break;
            case R.id.btnPrv:
                prvMusic();
                break;
            case R.id.btnNext:
                nextMusic();
                break;
            case R.id.tracksBtn:
                curPlaylist = "allTracks";
                updatePlayList();
                break;
            case R.id.favPlaylistBtn:
                curPlaylist = "favourites";
                updatePlayList();
                break;
            case R.id.favBtn:
                pm.addToPlaylist("favourites", pm.getFromPlaylist(curPlaylist, songPosition));
        }
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource((pm.getFromPlaylist(curPlaylist, songPosition)).getPath());
                //set song information
                if (isPaused == false) {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    playButton.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            } catch (IOException e) {
                Log.v(getString(R.string.app_name), e.getMessage());
            }
        }
    }

    public void pauseMusic () {
        if (mediaPlayer != null){
            isPaused = true;
            mediaPlayer.pause();
            //text.setText("Music paused");
            playButton.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        }
    }

    public void unPause() {
        if (mediaPlayer != null) {
            isPaused = false;
            mediaPlayer.start();
            playButton.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }
    }

    public void prvMusic() {
        if (songPosition != 0) {
            songPosition--;
        } else {
            songPosition = pm.getPlaylistSize(curPlaylist);
        }

        playMusic();
    }

    public void nextMusic() {
        if (songPosition != pm.getPlaylistSize(curPlaylist)) {
            songPosition++;
        } else {
            songPosition = 0;
        }

        playMusic();
    }

    public void updatePlayList() {
        listView = (ListView)findViewById(R.id.listView);
        songPosition = 0;
        ArrayList<song> cur = pm.playlists(curPlaylist);

        if (cur == null) {
            Toast.makeText(this, "opening playlist failed", Toast.LENGTH_LONG).show();
        } else if (cur.size() == 0) {
            Toast.makeText(this, "empty playlist", Toast.LENGTH_LONG).show();
        } else {
            customListView = new CustomListViewAdaptor(getApplicationContext(), cur);
            listView.setAdapter(customListView);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songPosition = position;
                isPaused = false;
                playMusic();
            }
        });
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();

        //save playlists
        pm.savePlaylists(getApplicationContext());

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}