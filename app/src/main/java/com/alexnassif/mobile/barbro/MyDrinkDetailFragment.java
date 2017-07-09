package com.alexnassif.mobile.barbro;


import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.alexnassif.mobile.barbro.data.BarBroContract;

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
    private String picFile;
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
        setHasOptionsMenu(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditDrinkActivity.class);
                intent.putExtra("drink", drinkId);
                startActivity(intent);

            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.editdrink, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {

            Uri uri = BarBroContract.MyDrinkEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(drinkId)).build();
            if(picFile != null){
                File file = new File(picFile);
                if(file.exists()) {
                    file.delete();
                }

            }

            AsyncQueryHandler deleteDrink = new AsyncQueryHandler(getContext().getContentResolver()) {

                @Override
                protected void onDeleteComplete(int token, Object cookie, int result) {
                    super.onDeleteComplete(token, cookie, result);
                    getActivity().finish();
                }
            };
            deleteDrink.startDelete(-1, null, uri, null, null);

        }
        if(id == R.id.edit_drink_menu){
            Intent intent = new Intent(getContext(), EditDrinkActivity.class);
            intent.putExtra("drink", drinkId);
            startActivity(intent);
        }
        return true;
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

        if(data.getCount() > 0) {
            data.moveToFirst();

            int drinkName = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
            int ingredients = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
            int picId = data.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);

            mMyDrinkName.setText(data.getString(drinkName));
            mMyDrinkIngredients.setText(data.getString(ingredients));
            picFile = data.getString(picId);
            if (picFile != null) {
                Uri takenPhotoUri = Uri.fromFile(new File(picFile));
                Glide.with(getContext()).load(takenPhotoUri.getPath()).into(mMyDrinkImage);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}
