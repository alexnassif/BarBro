package com.example.raymond.barbro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.Drink;

/**
 * Created by raymond on 12/12/16.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkAdapterViewHolder> {

    private Drink[] mDrinkData;

    private final DrinkAdapterOnClickHandler mClickHandler;

    public DrinkAdapter(DrinkAdapterOnClickHandler handler) {
        mClickHandler = handler;
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
        Drink drink = mDrinkData[position];
        Glide.with(holder.mDrinkImageView.getContext()).load("http://assets.absolutdrinks.com/drinks/300x400/" + drink.getId() +".png").into(holder.mDrinkImageView);
        holder.mDrinkTextView.setText(drink.toString());

    }

    @Override
    public int getItemCount() {
        if(mDrinkData == null)
            return 0;
        return mDrinkData.length;
    }

    public void setDrinkData(Drink[] drinkData) {
        mDrinkData = drinkData;
        notifyDataSetChanged();
    }

    public class DrinkAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final TextView mDrinkTextView;
        public final ImageView mDrinkImageView;
        public DrinkAdapterViewHolder(View itemView) {
            super(itemView);
            mDrinkImageView = (ImageView) itemView.findViewById(R.id.drink_image);
            mDrinkTextView = (TextView) itemView.findViewById(R.id.drink_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Drink drink = mDrinkData[adapterPosition];
            mClickHandler.onClick(drink);
        }
    }
}
