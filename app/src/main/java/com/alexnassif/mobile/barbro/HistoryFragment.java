package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModel;
import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.data.HistoryUtils;

import java.util.List;


public class HistoryFragment extends Fragment implements DrinkAdapter.DrinkAdapterOnClickHandler {

    private static final int HISTORY_SEARCH_LOADER = 22;
    private static final int DRINK_BY_ID_LOADER = 24;

    private RecyclerView mRecyclerView;

    private DrinkAdapter mDrinkAdapter;
    private View myView;
    private AutoCompleteTextView acDrinkTextView;
    private VideoFragment videoFragment;
    private DrinkDetailFragment drinkDetailFragment;

    private boolean mDualPane;
    int mCurCheckPosition = 0;
    private String videoURL = "pennsylvania.mp4";
    private YouTubeLayout youtubeLayout;
    private TextView viewHeader;
    private int drinkId;
    private TextView viewDesc;
    private TextView mMixView;
    private boolean whichFragment = true;
    private Menu mMenu;
    private MenuInflater mMenuInflater;
    private boolean isMenu = false;
    private ImageView mArrowExit;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new DrinkAdapter(getContext(), this);
        mRecyclerView.setAdapter(mDrinkAdapter);
        View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        getActivity().setTitle("History");

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 1);
            if(mDualPane) {
                whichFragment = savedInstanceState.getBoolean("fragment");
                if (whichFragment)
                    drinkDetailFragment = (DrinkDetailFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
                else
                    videoFragment = (VideoFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            }
        }

        if (mDualPane) {
            if(mCurCheckPosition == 0)
                mCurCheckPosition = 1;
            if(whichFragment)
                showDetails(mCurCheckPosition);
            else {
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                fragmentManager
                        .replace(R.id.drink_detail_fragment, videoFragment)
                        .commit();
            }
        }
        mArrowExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeLayout.setVisibility(View.GONE);
                mMenu.clear();
                isMenu = false;
                mMenuInflater.inflate(R.menu.history, mMenu);
            }
        });
        DrinksViewModel model = ViewModelProviders.of(this).get(DrinksViewModel.class);
        model.getDrinks().observe(this, new Observer<List<DrinkList>>() {
            @Override
            public void onChanged(List<DrinkList> drinkLists) {
                mDrinkAdapter.swapCursor(drinkLists);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
        if(mDualPane) {
            if (whichFragment)
                getFragmentManager().putFragment(outState, "myFragmentName", drinkDetailFragment);
            else
                getFragmentManager().putFragment(outState, "myFragmentName", videoFragment);

            outState.putBoolean("fragment", whichFragment);
        }
    }
    void showDetails(int index) {
        whichFragment = true;
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.

            // Check what fragment is currently shown, replace if needed.
            //DrinkDetailFragment details = DrinkDetailFragment.newInstance(index);

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            /*FragmentTransaction ft = getFragmentManager().beginTransaction();
            drinkDetailFragment = DrinkDetailFragment.newInstance(index);
            ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
            ft.commit();*/


        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_history, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);

        acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.search_drinks);
        if (!mDualPane) {
            viewHeader = (TextView) myView.findViewById(R.id.header);
            viewDesc = (TextView) myView.findViewById(R.id.desc);
            mMixView = (TextView) myView.findViewById(R.id.desc_view_youtube);
            youtubeLayout = (YouTubeLayout) myView.findViewById(R.id.dragLayout);
            mArrowExit = (ImageView) youtubeLayout.findViewById(R.id.arrowUpExit);
        }
        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);

        mMenu = menu;
        mMenuInflater = inflater;


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.video_item && mCurCheckPosition != 0) {

            Intent intent = new Intent(getContext(), VideoActivity.class);
            intent.putExtra("video", mCurCheckPosition);
            startActivity(intent);

        }
        else if(id == R.id.history_item){
            AsyncQueryHandler deleteHistory = new AsyncQueryHandler(getContext().getContentResolver()){};
            Uri uriAllDrinks = BarBroContract.HistoryEntry.CONTENT_URI;
            deleteHistory.startDelete(-1, null, uriAllDrinks, null, null);


        }
        else
            Toast.makeText(getContext(), "No Drink Chosen", Toast.LENGTH_LONG).show();


        return true;
    }
    @Override
    public void onClick(DrinkList drink) {

        //HistoryUtils.addToHistory(getContext(), drink);
        if (mDualPane) {
            showDetails(Integer.parseInt(drink.getIdDrink()));
        }
        else
            drinkDetail(Integer.parseInt(drink.getIdDrink()));

    }
    public void drinkDetail(int drink){
        showVideoIcon();
        mCurCheckPosition = drink;
        drinkId = drink;

    }
    private void showVideoIcon(){

        if(!isMenu) {
            mMenuInflater.inflate(R.menu.video, mMenu);
            isMenu = true;
        }

    }

}
