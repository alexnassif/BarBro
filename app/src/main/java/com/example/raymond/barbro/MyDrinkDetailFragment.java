package com.example.raymond.barbro;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;
import com.google.android.gms.plus.PlusOneButton;

import java.io.File;

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
    private static final int MY_DRINK_DETAIL_LOADER = 1;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MY_DRINK_DETAIL_LOADER, null, this);
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
        switch (id){
            case MY_DRINK_DETAIL_LOADER:{
                String stringId = Integer.toString(drinkId);
                Uri uri = BarBroContract.MyDrinkEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                return new CursorLoader(getContext(),
                        uri,
                        null,
                        null,
                        null,
                        null);}
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        int drinkName = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
        int ingredients = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
        int picId = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);

        mMyDrinkName.setText(data.getString(drinkName));
        mMyDrinkIngredients.setText(data.getString(ingredients));
        String pic = data.getString(picId);
        if (pic != null) {
            Uri takenPhotoUri = Uri.fromFile(new File(pic));
            Glide.with(getContext()).load(takenPhotoUri.getPath()).into(mMyDrinkImage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
