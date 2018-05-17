package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.alexnassif.mobile.barbro.model.Drink;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by mobile on 12/12/16.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkAdapterViewHolder> {

    private List<Drink> mDrinkData;
    private Context context;
    private final DrinkAdapterOnClickHandler mClickHandler;

    public DrinkAdapter(Context context, DrinkAdapterOnClickHandler handler) {
        mClickHandler = handler;
        this.context = context;
    }

    public interface DrinkAdapterOnClickHandler{
        void onClick(int drink);
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


        Drink drink = mDrinkData.get(position);

        //final int id = mDrinkData.getInt(drinkId);
        String _drinkName = drink.getDrinkName();
        String drinkPic = drink.getPic();
        int fave = drink.getFavorite();
        //holder.itemView.setTag(id);

        Glide.with(holder.mDrinkImageView.getContext()).load("http://assets.absolutdrinks.com/drinks/300x400/" + drinkPic +".png").into(holder.mDrinkImageView);
        holder.mDrinkTextView.setText(_drinkName);
        if(fave == 1) {
            holder.mFaveButtonView.setImageResource(R.drawable.ic_fave);

        }
        else
            holder.mFaveButtonView.setImageResource(R.drawable.ic_non_fave);

    }

    @Override
    public int getItemCount() {
        if(mDrinkData == null)
            return 0;
        return mDrinkData.size();
    }

    void swapCursor(List<Drink> drinkData) {
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
            mFaveButtonView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            /*int adapterPosition = getAdapterPosition();
            mDrinkData.moveToPosition(adapterPosition);
            int drinkId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry._ID);
            int faveId = mDrinkData.getColumnIndex(BarBroContract.BarBroEntry.COLUMN_FAVORITE);
            final int idh = mDrinkData.getColumnIndex(BarBroContract.HistoryEntry.COLUMN_HISTORYID);

            int id = mDrinkData.getInt(drinkId);
            int fave = mDrinkData.getInt(faveId);
            int history_id = 0;
            if(idh != -1){
                history_id = mDrinkData.getInt(idh);
            }
            final String stringID = Integer.toString(id);

            if (view.getId() == mFaveButtonView.getId()) {
                AsyncQueryHandler putDrink = new AsyncQueryHandler(context.getContentResolver()) {

                    @Override
                    protected void onUpdateComplete(int token, Object cookie, int result) {
                        super.onUpdateComplete(token, cookie, result);
                        if(idh != -1)
                            context.getContentResolver().notifyChange(BarBroContract.HistoryEntry.CONTENT_URI, null);
                    }
                };
                ContentValues mUpdateValues = new ContentValues();
                Uri uri = BarBroContract.BarBroEntry.CONTENT_URI;
                if(idh == -1)
                    uri = uri.buildUpon().appendPath(stringID).build();
                else
                    uri = uri.buildUpon().appendPath(history_id + "").build();
                if (fave == 1) {
                    mUpdateValues.put(BarBroContract.BarBroEntry.COLUMN_FAVORITE, 0);
                } else {
                    mUpdateValues.put(BarBroContract.BarBroEntry.COLUMN_FAVORITE, 1);

                }
                putDrink.startUpdate(-1, null, uri, mUpdateValues, null, null);


            } else {
                if(idh != -1)
                    mClickHandler.onClick(history_id);
                else
                    mClickHandler.onClick(id);
            }*/
        }
    }
}
