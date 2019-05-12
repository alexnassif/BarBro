package com.alexnassif.mobile.barbro;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.ViewModel.FavoriteDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoritesDetailViewModel;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.utilities.InjectorUtils;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteDetailFragment extends Fragment {
    private static final String drinkId = "drink";
    private View myView;
    private ImageView mImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private TextView mRecipe;
    private MenuItem favoriteMenuItem;
    private FavoritesDetailViewModel drinkModel;
    private boolean faveFlag = false;
    private AppDatabase mDb;
    private DrinkList mCurrentDrink;
    private Drink mDrink;
    private int drink;
    public FavoriteDetailFragment() {
        // Required empty public constructor
    }

    public static FavoriteDetailFragment newInstance(int drink) {
        FavoriteDetailFragment fragment = new FavoriteDetailFragment();
        Bundle args = new Bundle();
        args.putInt(drinkId, drink);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            drink = getArguments().getInt(drinkId);
        }

        FavoritesDetailViewModel model = ViewModelProviders.of(getActivity()).get(FavoritesDetailViewModel.class);
        model.getDrinkLV().observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drinkId) {
                mDrink = drinkId;
                drink = drinkId.getIdDrink();
                Log.d("drinktitle", drinkId.getStrDrink());
                mDrinkTitle.setText(drinkId.getStrDrink());
                mIngredients.setText(drinkId.drinkIngredients());
                mRecipe.setText(drinkId.getStrInstructions());
                Glide.with(mImageView.getContext()).load(drinkId.getStrDrinkThumb()).into(mImageView);
            }
        });

        /*FavoriteDetailViewModelFactory faveDetailFactory = InjectorUtils.provideFavoriteDetailViewModelFactory(getContext().getApplicationContext());

        drinkModel = ViewModelProviders.of(this, faveDetailFactory).get(FavoritesDetailViewModel.class);
        drinkModel.setDrinkId(drink);
        drinkModel.getFave().observe(this, new Observer<DrinkList>() {
            @Override
            public void onChanged(DrinkList drinkList) {

                if(drinkList != null){

                    mCurrentDrink = drinkList;
                    faveFlag = true;
                    // getActivity().invalidateOptionsMenu();

                }

            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.drink_detail_novideo, container, false);
        mDrinkTitle = (TextView) myView.findViewById(R.id.drink_name_novideo);
        mIngredients = (TextView) myView.findViewById(R.id.drink_ingredients_novideo);
        mImageView = (ImageView) myView.findViewById(R.id.drink_pic_novideo);
        mRecipe = (TextView) myView.findViewById(R.id.drink_recipe_novideo);
        return myView;
    }


}
