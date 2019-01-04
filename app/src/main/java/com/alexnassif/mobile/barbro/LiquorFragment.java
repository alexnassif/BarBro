package com.alexnassif.mobile.barbro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.List;


public class LiquorFragment extends Fragment implements DrinkAdapter.DrinkAdapterOnClickHandler {

    private static final int GITHUB_SEARCH_LOADER = 22;

    private RecyclerView mRecyclerView;

    private DrinkAdapter mDrinkAdapter;

    private View myView;
    private MultiSpinner mLiquorSpinner;
    private String liqType;
    private AutoCompleteTextView mAutoCompleteTextView;
    private boolean mDualPane;
    int mCurCheckPosition = 0;
    private String videoURL = "pennsylvania.mp4";
    private static final int DRINK_BY_ID_LOADER = 24;
    private YouTubeLayout youtubeLayout;
    private TextView viewHeader;
    private int drinkId;
    private TextView viewDesc;
    private TextView mMixView;
    private boolean whichFragment = true;
    private VideoFragment videoFragment;
    private DrinkDetailFragment drinkDetailFragment;
    ArrayAdapter<CharSequence> adapter;
    private Menu mMenu;
    private MenuInflater mMenuInflater;
    private boolean isMenu = false;
    private ImageView mArrowExit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDrinkAdapter = new DrinkAdapter(getContext(), this);
        mRecyclerView.setAdapter(mDrinkAdapter);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.liquor_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLiquorSpinner.setAdapter(adapter, false, onSelectedListener);
        boolean[] selectedItems = new boolean[adapter.getCount()];
        //selectedItems[1] = true; // select second item
        mLiquorSpinner.setSelected(selectedItems);
        mLiquorSpinner.setText("Pick your Flavor");

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
                //videoFragment = videoFragment.newInstance(videoURL);
                fragmentManager
                        .replace(R.id.drink_detail_fragment, videoFragment)
                        .commit();
            }
        }

        if(!mDualPane) {
            mArrowExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    youtubeLayout.setVisibility(View.GONE);
                    mMenu.clear();
                    isMenu = false;
                }
            });
        }

        DrinksViewModel model = ViewModelProviders.of(this).get(DrinksViewModel.class);
        model.getDrinks().observe(this, new Observer<List<DrinkList>>() {
            @Override
            public void onChanged(List<DrinkList> drinkLists) {
                mDrinkAdapter.swapCursor(drinkLists);
            }
        });
    }

    @Override
    public void onDestroy() {
        getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        super.onDestroy();
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
            /**FragmentTransaction ft = getFragmentManager().beginTransaction();
            drinkDetailFragment = DrinkDetailFragment.newInstance(index);
            ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);


            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();*/


        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.liq_layout, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.recyclerview_drinks);
        mAutoCompleteTextView = (AutoCompleteTextView) myView.findViewById(R.id.drink_autoCompleteTextView);
        mLiquorSpinner = (MultiSpinner) myView.findViewById(R.id.liquor_spinner);
        mAutoCompleteTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        getActivity().setTitle("Search by Type");
        if (!mDualPane) {
            viewHeader = (TextView) myView.findViewById(R.id.header);
            viewDesc = (TextView) myView.findViewById(R.id.desc);
            mMixView = (TextView) myView.findViewById(R.id.desc_view_youtube);
            youtubeLayout = (YouTubeLayout) myView.findViewById(R.id.dragLayout);
            mArrowExit = (ImageView) myView.findViewById(R.id.arrowUpExit);
            //youtubeLayout.setVisibility(View.GONE);
        }
        return myView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (mDualPane) {
            inflater.inflate(R.menu.video, menu);
        } else {
            mMenu = menu;
            mMenuInflater = inflater;
        }

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
        else
            Toast.makeText(getContext(), "No Drink Chosen", Toast.LENGTH_LONG).show();


        return true;
    }



    @Override
    public void onClick(int drink) {
        HistoryUtils.addToHistory(getContext(), drink);
        if (mDualPane) {
            showDetails(drink);
        }
        else
            drinkDetail(drink);

    }
    public void drinkDetail(int drink){
        mCurCheckPosition = drink;
        drinkId = drink;
        if(!isMenu) {
            mMenuInflater.inflate(R.menu.video, mMenu);
            isMenu = true;
        }

//        Intent intent = new Intent(getContext(), DrinkDetailActivity.class);
//        intent.putExtra("drink", drink);
//        startActivity(intent);
    }
    private boolean checkForTastes(boolean[] tastes){

        for(int i = 0; i < tastes.length; i++) {
            if (tastes[i] == true)
                return true;
        }
        return false;
    }
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
       // Resources res = getResources();
        //String[] sArray = res.getStringArray(R.array.liquor_array);
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            liqType = "";
            Resources res = getResources();
            String[] sArray = res.getStringArray(R.array.liquor_array);
            ArrayList<String> sList = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            if(checkForTastes(selected)) {
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        sList.add(adapter.getItem(i).toString());
                    }
                }

                for (int i = 0; i < sList.size(); i++) {
                    if (i != sList.size() - 1)
                        builder.append(sList.get(i)).append(" AND ");
                    else
                        builder.append(sList.get(i));
                }
                liqType = builder.toString();

            }
            else{
                Toast.makeText(getContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
                mLiquorSpinner.setText("Pick your Flavor");
            }
        }
    };

}
