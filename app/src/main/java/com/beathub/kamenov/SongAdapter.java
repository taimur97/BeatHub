package com.beathub.kamenov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SongAdapter extends ArrayAdapter<Song>{

    private Context context;
    private int layout_id;
    private ArrayList<Song> songs;
    private ViewHelper helper;

    SongAdapter(Context context, int layout_id, ArrayList<Song> songs){
        super(context, layout_id, songs);
        this.context = context;
        this.layout_id = layout_id;
        this.songs = songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;

        if(view == null){

            helper = new ViewHelper();

            view = inflater.inflate(layout_id, parent, false);
            helper.songTitle = (TextView) view.findViewById(R.id.song_title);
            helper.artistName = (TextView) view.findViewById(R.id.song_artist);
            helper.songDuration = (TextView) view.findViewById(R.id.duration);

            view.setTag(helper);
        }

        Song song = songs.get(position);

        helper = (ViewHelper) view.getTag();

        helper.songTitle.setText(song.getTitle());
        helper.artistName.setText(song.getArtist());
        helper.songDuration.setText(Utils.durationFormat(song.getDuration()));

        return view;
    }

    class ViewHelper{

        TextView songTitle;
        TextView artistName;
        TextView songDuration;

    }
}
