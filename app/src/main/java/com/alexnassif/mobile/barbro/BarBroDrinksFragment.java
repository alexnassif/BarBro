package com.alexnassif.mobile.barbro;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexnassif.mobile.barbro.Adapters.BarBroDrinkAdapter;
import com.alexnassif.mobile.barbro.ViewModel.BarBroDrinksViewModel;
import com.alexnassif.mobile.barbro.ViewModel.BarBroDrinksViewModelFactory;
import com.alexnassif.mobile.barbro.data.BarBroDrink;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarBroDrinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarBroDrinksFragment extends Fragment implements BarBroDrinkAdapter.BarBroDrinkOnClickHandler {
    private RecyclerView mRecyclerView;
    private BarBroDrinkAdapter mDrinkAdapter;
    private View myView;

    //ViewModels
    private BarBroDrinksViewModel model;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_bar_bro_drinks, container, false);
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.barbro_recyclerview);
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

        model.getDrinks().observe(this, new Observer<List<BarBroDrink>>() {
            @Override
            public void onChanged(List<BarBroDrink> barBroDrinks) {
                mDrinkAdapter = new BarBroDrinkAdapter(getContext(), BarBroDrinksFragment.this);
                mRecyclerView.setAdapter(mDrinkAdapter);
                mDrinkAdapter.swapList(barBroDrinks);
            }
        });
    }

    @Override
    public void onClick(BarBroDrink drink) {

        Intent intent = new Intent(getContext(), BarBroDrinkDetailActivity.class);
        intent.putExtra("drink", drink);
        startActivity(intent);


    }
}
