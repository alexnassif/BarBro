package com.example.raymond.barbro;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.example.raymond.barbro.data.BarBroContract;


public class EditDrinkFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private EditText mNewDrink;
    private EditText mNewIngredients;
    private ImageView mAddImage;
    private Button mSubmit;
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int MY_DRINKS_LOADER = 1;
    // TODO: Rename and change types of parameters
    private int drinkId;

    public EditDrinkFragment() {
        // Required empty public constructor
    }


    public static EditDrinkFragment newInstance(int drink) {
        EditDrinkFragment fragment = new EditDrinkFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, drink);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_drink, container, false);
        mNewDrink = (EditText) view.findViewById(R.id.edit_drink);
        mNewIngredients = (EditText) view.findViewById(R.id.new_drink_ingredients);
        mAddImage = (ImageView) view.findViewById(R.id.take_drink_pic);
        mSubmit = (Button) view.findViewById(R.id.submit_button);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        getLoaderManager().initLoader(MY_DRINKS_LOADER, null, this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case MY_DRINKS_LOADER: {
                Uri uriAllDrinks = BarBroContract.MyDrinkEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        uriAllDrinks,
                        null,
                        BarBroContract.BarBroEntry._ID + "=?",
                        new String[]{String.valueOf(drinkId)},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        int drinkName = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
        int ingredients = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
        int videoId = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);

        mNewDrink.setText(data.getString(drinkName));
        mNewIngredients.setText(data.getString(ingredients));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
