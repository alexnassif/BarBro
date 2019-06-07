package com.alexnassif.mobile.barbro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModel;
import com.alexnassif.mobile.barbro.ViewModel.CheckFavoriteViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoriteDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoriteViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoritesDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.FavoritesViewModel;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteFragment extends Fragment implements DrinkAdapter.DrinkAdapterOnClickHandler {

        private RecyclerView mRecyclerView;
        private DrinkAdapter mDrinkAdapter;
        private View myView;
        private AutoCompleteTextView acDrinkTextView;

        private boolean mDualPane;
        int mCurCheckPosition = 0;
        private DrinkDetailFragment drinkDetailFragment;

        //ViewModels
        private FavoritesViewModel model;
        private DrinkDetailViewModel drinkModel;
        private CheckFavoriteViewModel checkFaveModel;

        private Menu mMenu;
        private MenuInflater mMenuInflater;
        private boolean isMenu = false;
        private AppDatabase mDb;
        private DrinkList faveItem;


        public static FavoriteFragment newInstance() {
            FavoriteFragment fragment = new FavoriteFragment();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FavoriteViewModelFactory faveFactory = InjectorUtils.provideFavoriteViewModelFactory(getContext().getApplicationContext());
            model = ViewModelProviders.of(this, faveFactory).get(FavoritesViewModel.class);
            DrinkDetailViewModelFactory drinkDetailFactory = InjectorUtils.provideDrinkDetailViewModelFactory(getContext().getApplicationContext());
            drinkModel = ViewModelProviders.of(getActivity(), drinkDetailFactory).get(DrinkDetailViewModel.class);
            CheckFavoriteViewModelFactory checkFaveFactory = InjectorUtils.provideCheckFavoriteViewModelFactory(getContext().getApplicationContext());
            checkFaveModel = ViewModelProviders.of(getActivity(), checkFaveFactory).get(CheckFavoriteViewModel.class);
            mDb = AppDatabase.getsInstance(getContext());
            setHasOptionsMenu(true);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            View detailsFrame = getActivity().findViewById(R.id.drink_detail_fragment);
            mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
            getActivity().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            getActivity().setTitle("Favorites");


            if (savedInstanceState != null) {
                // Restore last state for checked position.
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 1);
                if(mDualPane) {
                    drinkDetailFragment = (DrinkDetailFragment) getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
                }
            }

            if (mDualPane) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                drinkDetailFragment = drinkDetailFragment.newInstance(mCurCheckPosition);
                ft.replace(R.id.drink_detail_fragment, drinkDetailFragment);
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

            }

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            model.getFavorites().observe(this, new Observer<List<DrinkList>>() {
                @Override
                public void onChanged(List<DrinkList> drinkLists) {

                    if(drinkLists.size() == 0)
                        Toast.makeText(getContext(), "You haven't added any favorite drinks yet.", Toast.LENGTH_LONG).show();

                    mDrinkAdapter = new DrinkAdapter(getContext(), FavoriteFragment.this);
                    mRecyclerView.setAdapter(mDrinkAdapter);
                    mDrinkAdapter.swapCursor(drinkLists);
                    DrinkList[] array = new DrinkList[drinkLists.size()];
                    array = drinkLists.toArray(array);
                    ArrayAdapter<DrinkList> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, array);
                    acDrinkTextView.setAdapter(adapter);

                    acDrinkTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            DrinkList drink = (DrinkList) adapterView.getAdapter().getItem(i);
                            drinkDetail(Integer.parseInt(drink.getIdDrink()));
                            acDrinkTextView.setText("");

                        }
                    });
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
                getFragmentManager().putFragment(outState, "myFragmentName", drinkDetailFragment);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myView = inflater.inflate(R.layout.favorite_layout, container, false);
            mRecyclerView = (RecyclerView) myView.findViewById(R.id.fave_recyclerview_drinks);
            acDrinkTextView = (AutoCompleteTextView) myView.findViewById(R.id.fave_search_drinks);

            return myView;
        }



        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(final MenuItem item) {


            return super.onOptionsItemSelected(item);
        }
        private void drinkDetail(int drinkId){
            mCurCheckPosition = drinkId;
            drinkModel.setDrink(drinkId);
            checkFaveModel.setDrink(drinkId);
            if(!mDualPane){

                Intent drinkdetailIntent = new Intent(getContext(), DrinkDetailActivity.class);
                drinkdetailIntent.putExtra("drink", drinkId);
                startActivity(drinkdetailIntent);
            }


        }

        @Override
        public void onClick(DrinkList drinkId) {

            this.faveItem = drinkId;
            drinkDetail(Integer.parseInt(drinkId.getIdDrink()));
        }
}
