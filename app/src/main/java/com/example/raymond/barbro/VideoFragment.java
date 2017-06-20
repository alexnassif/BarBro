package com.example.raymond.barbro;


import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.raymond.barbro.data.BarBroContract;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private VideoView videoView;
    private static final String video_url = "videoUrl";
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    private int mParam1;
    private View myView;
    private ProgressBar progressBar;
    private int videoPoint = 0;
    private static final int DRINK_BY_ID_LOADER = 24;


    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance(int param1) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(video_url, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(video_url);
        }
        if (savedInstanceState != null) {
            mParam1 = savedInstanceState.getInt("video");
            videoPoint = savedInstanceState.getInt("position");

        }
        // Start lengthy operation in a background thread
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_video, container, false);

        progressBar = (ProgressBar) myView.findViewById(R.id.video_progressbar);
        videoView = (VideoView) myView.findViewById(R.id.video_fragment_view);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(DRINK_BY_ID_LOADER, null, this);
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if (!isConnected)
                    Toast.makeText(getContext(), "No Network Connectivity. Cannot Play Video", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                /*
                 * add media controller
                 */
                        final MediaController mediaController = new MediaController(getContext());
                        videoView.setMediaController(mediaController);
                /*
                 * and set its position on screen
                 */
                        mediaController.setAnchorView(videoView);

                    }
                });

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("video", mParam1);
        outState.putInt("position", videoView.getCurrentPosition());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DRINK_BY_ID_LOADER: {
                String stringId = Integer.toString(mParam1);
                Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                return new CursorLoader(getContext(),
                        uri,
                        null,
                        null,
                        null,
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == DRINK_BY_ID_LOADER) {
            data.moveToFirst();
            int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);

            String videoURL = data.getString(videoId);
            progressBar.setVisibility(View.GONE);
            if (videoURL != null) {
                videoView.setVideoPath(videoUrl + videoURL);
                videoView.seekTo(videoPoint);
                videoView.start();
            } else {
                Toast.makeText(getContext(), "No Video Available for this Drink", Toast.LENGTH_LONG).show();
                videoView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
