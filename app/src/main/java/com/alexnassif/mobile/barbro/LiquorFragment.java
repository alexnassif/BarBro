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

import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModelFactory;
import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.data.HistoryUtils;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.List;


public class LiquorFragment extends Fragment implements DrinkAdapter.DrinkAdapterOnClickHandler {


    private RecyclerView mRecyclerView;

    private DrinkAdapter mDrinkAdapter;

    private View myView;
    private MultiSpinner mLiquorSpinner;
    private String liqType;
    private AutoCompleteTextView mAutoCompleteTextView;
    private boolean mDualPane;
    int mCurCheckPosition = 0;
    private boolean whichFragment = true;
    private DrinkDetailFragment drinkDetailFragment;
    ArrayAdapter<CharSequence> adapter;
    private boolean isMenu = false;

    private DrinksViewModel drinksModel;
    private DrinkDetailViewModel drinkDetailModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DrinkDetailViewModelFactory detailFactory = InjectorUtils.provideDrinkDetailViewModelFactory(getContext().getApplicationContext());
        drinkDetailModel = ViewModelProviders.of(getActivity(), detailFactory).get(DrinkDetailViewModel.class);
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
                drinkDetailFragment = (DrinkDetailFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            }
        }


        if (mDualPane) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            drinkDetailFragment = drinkDetailFragment.newInstance();
            ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        }

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
            getFragmentManager().putFragment(outState, "myFragmentName", drinkDetailFragment);
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

        return myView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return true;
    }



    public void drinkDetail(int drinkId){
        mCurCheckPosition = drinkId;
        drinkDetailModel.setDrink(drinkId);
        if(!mDualPane){

            Intent drinkdetailIntent = new Intent(getContext(), DrinkDetailActivity.class);
            drinkdetailIntent.putExtra("drink", drinkId);
            startActivity(drinkdetailIntent);

        }


    }

    @Override
    public void onClick(DrinkList drinkId) {

        //HistoryUtils.addToHistory(getContext(), drink);

        drinkDetail(Integer.parseInt(drinkId.getIdDrink()));
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
                DrinksViewModelFactory factory = InjectorUtils.provideDrinksViewModelFactory(getContext().getApplicationContext());
                drinksModel = ViewModelProviders.of(LiquorFragment.this, factory).get(DrinksViewModel.class);
                drinksModel.getDrinks().observe(LiquorFragment.this, new Observer<List<DrinkList>>() {
                    @Override
                    public void onChanged(List<DrinkList> drinkLists) {
                        mDrinkAdapter.swapCursor(drinkLists);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                });


            }
            else{
                Toast.makeText(getContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
                mLiquorSpinner.setText("Pick your Flavor");
            }
        }
    };

}
