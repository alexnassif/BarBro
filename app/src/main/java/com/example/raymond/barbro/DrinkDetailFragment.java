package com.example.raymond.barbro;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinkDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String drinkObj = "drinkId";
    private View myView;
    private int drinkId;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private VideoView mDrinkVideo;
    private String videoUrl = "http://assets.absolutdrinks.com/videos/";
    private static final int DRINK_SEARCH_LOADER = 1;
    private OnFragmentInteractionListener mListener;

    public DrinkDetailFragment() {
        // Required empty public constructor
    }


    public static DrinkDetailFragment newInstance(int drinkId) {
        DrinkDetailFragment fragment = new DrinkDetailFragment();
        Bundle args = new Bundle();
        args.putInt("drinkId", drinkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkId = getArguments().getInt(drinkObj);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.activity_drink_detail, container, false);
        mDrinkTitle = (TextView) myView.findViewById(R.id.drink_name);
        mIngredients = (TextView) myView.findViewById(R.id.drink_ingredients);
        mDrinkVideo = (VideoView) myView.findViewById(R.id.drink_video);

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case DRINK_SEARCH_LOADER: {
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry._ID + "=?",
                        new String[]{String.valueOf(drinkId)},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int drinkName = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
        int ingredients = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
        int videoId = data.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_VIDEO);

        mDrinkTitle.setText(data.getString(drinkName));
        mIngredients.setText(data.getString(ingredients));

        if(data.getString(videoId) != null) {
            String videoURL = videoUrl + data.getString(videoId);
            mDrinkVideo.setVideoPath(videoURL);
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            mDrinkVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if(!isConnected)
                        Toast.makeText(getContext(), "No Network Connectivity. Cannot Play Video", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            MediaController mediaController = new
                    MediaController(getContext());
            mediaController.setAnchorView(mDrinkVideo);
            mDrinkVideo.setMediaController(mediaController);
            mDrinkVideo.seekTo(5000);
        }
        else {
            Toast.makeText(getContext(), "No Video Available for this Drink", Toast.LENGTH_LONG).show();
            mDrinkVideo.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
