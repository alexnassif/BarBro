package com.alexnassif.mobile.barbro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;
import com.alexnassif.mobile.barbro.data.Drink;
import com.bumptech.glide.Glide;

public class DrinkDetailFragment extends Fragment {

    private View myView;
    private ImageView mImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private TextView mRecipe;

    public DrinkDetailFragment() {
        // Required empty public constructor
    }

    public static DrinkDetailFragment newInstance() {
        DrinkDetailFragment fragment = new DrinkDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DrinkDetailViewModel model = ViewModelProviders.of(getActivity()).get(DrinkDetailViewModel.class);
        model.getDrink().observe(this, new Observer<Drink>() {
            @Override
            public void onChanged(Drink drinkId) {
                mDrinkTitle.setText(drinkId.getStrDrink());
                mIngredients.setText(drinkId.drinkIngredients());
                mRecipe.setText(drinkId.getStrInstructions());
                Glide.with(mImageView.getContext()).load(drinkId.getStrDrinkThumb()).into(mImageView);
            }
        });
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
