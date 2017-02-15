package com.example.raymond.barbro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;




public class MainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, SmallDrinkAdapter.SmallDrinkAdapterOnClickHandler, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private boolean mParam1 = false;


    //private OnFragmentInteractionListener mListener;
    private static final int RANDOM_DRINKS_LOADER = 22;
    private static final int FAVE_DRINK_LOADER = 23;

    private RecyclerView mRecyclerView_Faves;
    private RecyclerView mRecyclerView_Randoms;

    private SmallDrinkAdapter mDrinkAdapter_Faves;
    private SmallDrinkAdapter mDrinkAdapter_Randoms;
    private Button faves_button;
    private Button randoms_button;
    private View myView;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(boolean param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager_faves
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_Faves.setLayoutManager(layoutManager_faves);
        mRecyclerView_Faves.setHasFixedSize(true);
        mDrinkAdapter_Faves = new SmallDrinkAdapter(getContext(), this);
        mRecyclerView_Faves.setAdapter(mDrinkAdapter_Faves);

        LinearLayoutManager layoutManager_randoms
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_Randoms.setLayoutManager(layoutManager_randoms);
        mRecyclerView_Randoms.setHasFixedSize(true);
        mDrinkAdapter_Randoms = new SmallDrinkAdapter(getContext(), this);
        mRecyclerView_Randoms.setAdapter(mDrinkAdapter_Randoms);

        faves_button.setOnClickListener(this);
        randoms_button.setOnClickListener(this);
        Loader<Cursor> loaderM = getLoaderManager().getLoader(FAVE_DRINK_LOADER);
        if(loaderM == null)
            getLoaderManager().initLoader(FAVE_DRINK_LOADER, null, this);
        else
            getLoaderManager().restartLoader(FAVE_DRINK_LOADER, null, this);

        getLoaderManager().initLoader(RANDOM_DRINKS_LOADER, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView_Randoms = (RecyclerView) myView.findViewById(R.id.random_recyclerView);
        mRecyclerView_Faves =  (RecyclerView) myView.findViewById(R.id.fave_recyclerView);
        faves_button = (Button) myView.findViewById(R.id.more_fave_button);
        randoms_button = (Button) myView.findViewById(R.id.random_button);
        getActivity().setTitle("Bar Bro");
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case RANDOM_DRINKS_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        null,
                        null,
                        " RANDOM() LIMIT 3");}
            case FAVE_DRINK_LOADER:{
                Uri uriAllDrinks = BarBroContract.BarBroEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry.COLUMN_FAVORITE + "=?",
                        new String[]{"1"},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null) {
            if(loader.getId() == FAVE_DRINK_LOADER)
                mDrinkAdapter_Faves.swapCursor(data);
            else
                mDrinkAdapter_Randoms.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int drink) {
        drinkDetail(drink);
    }

    public void drinkDetail(int drink){
        Intent intent = new Intent(getContext(), DrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        if(view.getId() == R.id.random_button) {
            getLoaderManager().restartLoader(RANDOM_DRINKS_LOADER, null, this);
        }
        else if(view.getId() == R.id.more_fave_button){
            fragmentManager
                    .replace(R.id.content_frame, ResultsFragment.newInstance(true))
                    .commit();
        }

        fragmentManager.addToBackStack(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
