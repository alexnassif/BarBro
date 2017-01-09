package com.example.raymond.barbro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;

/**
 * Created by raymond on 12/12/16.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkAdapterViewHolder> {

    private Cursor mDrinkData;
    private Context context;
    private final DrinkAdapterOnClickHandler mClickHandler;

    public DrinkAdapter(Context context, DrinkAdapterOnClickHandler handler) {
        mClickHandler = handler;
        this.context = context;
    }

    public interface DrinkAdapterOnClickHandler{
        void onClick(Drink drink);
    }

    @Override
    public DrinkAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.drink_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new DrinkAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrinkAdapterViewHolder holder, int position) {
        int drinkId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry._ID);
        int drinkName = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
        int ingredients = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
        int drinkPicId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);
        int faveId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_FAVORITE);

        mDrinkData.moveToPosition(position);

        final int id = mDrinkData.getInt(drinkId);
        final String stringID = Integer.toString(id);
        String _drinkName = mDrinkData.getString(drinkName);
        String drinkIngredients = mDrinkData.getString(ingredients);
        String drinkPic = mDrinkData.getString(drinkPicId);
        int fave = mDrinkData.getInt(faveId);
        holder.itemView.setTag(id);

        Glide.with(holder.mDrinkImageView.getContext()).load("http://assets.absolutdrinks.com/drinks/300x400/" + drinkPic +".png").into(holder.mDrinkImageView);
        holder.mDrinkTextView.setText(_drinkName);
        if(fave == 1) {
            holder.mFaveButtonView.setImageResource(R.drawable.ic_fave_drink);
            holder.mFaveButtonView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            ContentValues mUpdateValues = new ContentValues();
                            mUpdateValues.put(BarBroContract.BarBroEntry.COLUMN_FAVORITE, 0);
                            Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                            uri = uri.buildUpon().appendPath(stringID).build();
                            context.getContentResolver().update(uri, mUpdateValues, null, null);
                            return null;
                        }
                    }.execute();

                }
            });

        }
        else{
            holder.mFaveButtonView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            ContentValues mUpdateValues = new ContentValues();
                            mUpdateValues.put(BarBroContract.BarBroEntry.COLUMN_FAVORITE, 1);
                            Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                            uri = uri.buildUpon().appendPath(stringID).build();
                            context.getContentResolver().update(uri, mUpdateValues, null, null);
                            return null;
                        }
                    }.execute();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(mDrinkData == null)
            return 0;
        return mDrinkData.getCount();
    }

    void swapCursor(Cursor drinkData) {
        mDrinkData = drinkData;
        notifyDataSetChanged();
    }

    public class DrinkAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final TextView mDrinkTextView;
        public final ImageView mDrinkImageView;
        public final ImageButton mFaveButtonView;
        public DrinkAdapterViewHolder(View itemView) {
            super(itemView);
            mDrinkImageView = (ImageView) itemView.findViewById(R.id.drink_image);
            mDrinkTextView = (TextView) itemView.findViewById(R.id.drink_data);
            mFaveButtonView = (ImageButton) itemView.findViewById(R.id.fave_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            mDrinkData.moveToPosition(adapterPosition);
            int drinkId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry._ID);
            int drinkName = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME);
            int ingredients = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS);
            int drinkPicId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC);

            int id = mDrinkData.getInt(drinkId);
            String _drinkName = mDrinkData.getString(drinkName);
            String drinkIngredients = mDrinkData.getString(ingredients);
            String drinkPic = mDrinkData.getString(drinkPicId);

            Drink drink = new Drink(_drinkName, drinkIngredients, drinkPic);

            mClickHandler.onClick(drink);
        }
    }
}
