package com.beathub.kamenov;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import DataBases.BeatHubBaseHelper;

public class MainListsFragment extends Fragment {

    private static BeatHubBaseHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_fragment, null);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerListsFragments(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        db = new BeatHubBaseHelper(getActivity());

        return view;
    }

    protected static void addToPlayListDialog(final Activity activity,final Song song) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);

        View view = inflater.inflate(R.layout.add_to_playlist_dialog_layout, null);

        ListView playlists = (ListView) view.findViewById(R.id.playlists_list_view);
        PlaylistsAdapter adapter = new PlaylistsAdapter(activity, R.layout.playlist_simple_row_item, db.getAllPlaylists());
        adapter.notifyDataSetChanged();

        RelativeLayout createNewPlaylist = (RelativeLayout) view.findViewById(R.id.create_new_playlist);

        final AlertDialog alertDialog = dialog.create();

        createNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPlaylist(activity, song);
            }
        });

        playlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.addSongToPlaylist((int) song.getId(), position + 1);
                ((MainActivity)activity).showToastWithMessage("Added");
                ((MainActivity)activity).refreshMainListsFragment();
                alertDialog.dismiss();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        playlists.setAdapter(adapter);
        dialog.setView(view);
        dialog.create();
        dialog.show();
    }
    /*
    * position - position of the song in the list
    * */
    private static void createNewPlaylist(final Activity activity, final Song songForAdding){

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);

        View view = inflater.inflate(R.layout.create_new_playlist_dialog_layout, null);

        final EditText playlistName = (EditText) view.findViewById(R.id.new_playlist_name);

        dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (playlistName.getText().length() != 0) {

                    //create new playlist
                    db.addPlaylist(playlistName.getText().toString());

                    int playlist_id = db.getLastFreePositionToAddPlaylist();

                    db.addSongToPlaylist((int)songForAdding.getId(), playlist_id);
                    ((MainActivity)activity).refreshMainListsFragment();
                }
                ((MainActivity)activity).showToastWithMessage("Created");
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.create();
        dialog.show();

    }

}
