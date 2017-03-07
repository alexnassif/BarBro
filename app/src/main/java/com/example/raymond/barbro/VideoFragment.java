package com.example.raymond.barbro;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    private VideoView videoView;
    private static final String video_url = "videoUrl";
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    private String mParam1;
    private View myView;


    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance(String param1) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(video_url, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(video_url);
        }

        // Start lengthy operation in a background thread
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = (VideoView) myView.findViewById(R.id.video_fragment_view);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mParam1 != null) {
            videoView.setVideoPath(videoUrl + mParam1);

            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if(!isConnected)
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
            videoView.start();
        }
        else {
            Toast.makeText(getContext(), "No Video Available for this Drink", Toast.LENGTH_LONG).show();
            videoView.setVisibility(View.GONE);
        }
    }
}
