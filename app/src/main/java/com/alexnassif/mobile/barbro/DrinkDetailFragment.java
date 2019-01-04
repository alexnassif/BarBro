package com.alexnassif.mobile.barbro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.data.Drink;
import com.bumptech.glide.Glide;

public class DrinkDetailFragment extends Fragment {

    private String drinkObj = "drinkId";
    private View myView;
    private Drink drinkId;
    private ImageView mImageView;
    private TextView mDrinkTitle;
    private TextView mIngredients;
    private TextView mRecipe;

    public DrinkDetailFragment() {
        // Required empty public constructor
    }

    public static DrinkDetailFragment newInstance(Drink drinkId) {
        DrinkDetailFragment fragment = new DrinkDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("drinkId", drinkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            drinkId = (Drink) getArguments().getSerializable(drinkObj);
        }
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
        mDrinkTitle.setText(drinkId.getStrDrink());
        mIngredients.setText(drinkId.drinkIngredients());
        mRecipe.setText(drinkId.getStrInstructions());
        Glide.with(mImageView.getContext()).load(drinkId.getStrDrinkThumb()).into(mImageView);
    }
}
