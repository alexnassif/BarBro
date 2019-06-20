package com.alexnassif.mobile.barbro;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.alexnassif.mobile.barbro.Adapters.BarBroDrinkAdapter;
import com.alexnassif.mobile.barbro.ViewModel.BarBroDrinksViewModel;
import com.alexnassif.mobile.barbro.ViewModel.BarBroDrinksViewModelFactory;
import com.alexnassif.mobile.barbro.data.BarBroDrink;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.thomashaertel.widget.MultiSpinner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarBroDrinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarBroDrinksFragment extends Fragment implements BarBroDrinkAdapter.BarBroDrinkOnClickHandler {
    private RecyclerView mRecyclerView;
    private BarBroDrinkAdapter mDrinkAdapter;
    private View myView;
    private MultiSpinner mLiquorSpinner;
    private ArrayAdapter<CharSequence> adapter;
    //ViewModels
    private BarBroDrinksViewModel model;
    private Map<String, String> map;

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        // Resources res = getResources();
        //String[] sArray = res.getStringArray(R.array.liquor_array);
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            map.clear();

            if(checkForTastes(selected)) {
                for (int i = 0; i < selected.length; i++) {
                    if(selected[i])
                        map.put(adapter.getItem(i).toString().toLowerCase(), String.valueOf(selected[i]));

                }

            }
            else{
                Toast.makeText(getContext(), "Nothing Selected", Toast.LENGTH_LONG).show();
                mLiquorSpinner.setText("Pick your Flavor");
            }

            model.setDrinks(map);
        }
    };


    public BarBroDrinksFragment() {
        // Required empty public constructor
    }


    public static BarBroDrinksFragment newInstance() {
        BarBroDrinksFragment fragment = new BarBroDrinksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarBroDrinksViewModelFactory factory = InjectorUtils.provideBarBroDrinksVMFactory(getContext().getApplicationContext());
        model = ViewModelProviders.of(this, factory).get(BarBroDrinksViewModel.class);
        map = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_bar_bro_drinks, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.barbro_recyclerview);
        mLiquorSpinner = (MultiSpinner) myView.findViewById(R.id.liquor_spinner);
        // Inflate the layout for this fragment
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrinkAdapter = new BarBroDrinkAdapter(getContext(), BarBroDrinksFragment.this);
        mRecyclerView.setAdapter(mDrinkAdapter);

        model.getDrinkLV().observe(getViewLifecycleOwner(), new Observer<List<BarBroDrink>>() {
            @Override
            public void onChanged(List<BarBroDrink> barBroDrinks) {
                mDrinkAdapter.swapList(barBroDrinks);
            }
        });

        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.liquor_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLiquorSpinner.setAdapter(adapter, false, onSelectedListener);
        boolean[] selectedItems = new boolean[adapter.getCount()];
        //selectedItems[1] = true; // select second item
        mLiquorSpinner.setSelected(selectedItems);
        mLiquorSpinner.setText("Pick your Flavor");

        getActivity().setTitle("New Drinks");
    }

    @Override
    public void onClick(BarBroDrink drink) {

        Intent intent = new Intent(getContext(), BarBroDrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);


    }

    private boolean checkForTastes(boolean[] tastes){

        for(int i = 0; i < tastes.length; i++) {
            if (tastes[i])
                return true;
        }
        return false;
    }
}
