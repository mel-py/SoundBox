package com.example.melissa.soundbox;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Melissa on 2018-04-27.
 */

public class CustomListViewAdaptor extends BaseAdapter {
    private Context mContext;
    private ArrayList<song> songs;
    private static LayoutInflater inflater = null;

    public CustomListViewAdaptor (Context context, ArrayList<song> data) {
        mContext = context;
        songs = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.song_item, null);

            TextView title = (TextView)view.findViewById(R.id.title);
            TextView artist = (TextView)view.findViewById(R.id.artist);

            song mSong = songs.get(position);

            title.setText(mSong.getName());
            artist.setText(mSong.getArtist());
        }

        return view;
    }
}
