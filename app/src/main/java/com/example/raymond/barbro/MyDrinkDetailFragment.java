package com.example.raymond.barbro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Use the {@link MyDrinkDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDrinkDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int drinkId;
    private String drink_param = "drink";
    private TextView mMyDrinkName;
    private TextView mMyDrinkIngredients;
    private ImageView mMyDrinkImage;
    private Button mEditButton;

    public MyDrinkDetailFragment() {
        // Required empty public constructor
    }

    public static MyDrinkDetailFragment newInstance(int drinkId) {
        MyDrinkDetailFragment fragment = new MyDrinkDetailFragment();
        Bundle args = new Bundle();
        args.putInt("drink", drinkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkId = getArguments().getInt(drink_param);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_drink_detail, container, false);

        mMyDrinkImage = (ImageView) view.findViewById(R.id.my_drink_pic_view);
        mMyDrinkName = (TextView) view.findViewById(R.id.my_drink_name_view);
        mMyDrinkIngredients = (TextView) view.findViewById(R.id.my_ingredients_view);
        mEditButton = (Button) view.findViewById(R.id.edit_mydrink_button);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
