package com.alexnassif.mobile.barbro;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.alexnassif.mobile.barbro.data.DrinkList;
import com.bumptech.glide.Glide;
import com.alexnassif.mobile.barbro.data.BarBroContract;

import java.util.List;

/**
 * Created by mobile on 12/12/16.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkAdapterViewHolder> {

    private List<DrinkList> mDrinkData;
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
        DrinkList item = mDrinkData.get(position);
        //holder.itemView.setTag(id);

        Glide.with(holder.mDrinkImageView.getContext()).load(item.getStrDrinkThumb()).into(holder.mDrinkImageView);
        holder.mDrinkTextView.setText(item.getStrDrink());
        /*if(fave == 1) {
            holder.mFaveButtonView.setImageResource(R.drawable.ic_fave);

        }
        else
            holder.mFaveButtonView.setImageResource(R.drawable.ic_non_fave);*/

    }

    @Override
    public int getItemCount() {
        if(mDrinkData == null)
            return 0;
        return mDrinkData.size();
    }

    void swapCursor(List<DrinkList> drinkData) {
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

            int adapterPosition = getAdapterPosition();
            DrinkList item = mDrinkData.get(adapterPosition);
            mClickHandler.onClick(Integer.parseInt(item.getIdDrink()));
            Log.d("drinkadapterposition", item.getIdDrink());
        }
    }
}
