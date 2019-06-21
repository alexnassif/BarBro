package com.alexnassif.mobile.barbro.Adapters;


import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.alexnassif.mobile.barbro.R;
import com.alexnassif.mobile.barbro.data.Drink;
import com.bumptech.glide.Glide;

import java.util.List;

public class SmallDrinkAdapter extends RecyclerView.Adapter<SmallDrinkAdapter.SmallDrinkAdapterViewHolder> {

    private List<Drink> mDrinkData;
    private Context context;
    private final SmallDrinkAdapterOnClickHandler mClickHandler;

    public SmallDrinkAdapter(Context context, SmallDrinkAdapterOnClickHandler handler) {
        mClickHandler = handler;
        this.context = context;
    }

    public interface SmallDrinkAdapterOnClickHandler{
        void onClick(int drink);
    }
    @Override
    public SmallDrinkAdapter.SmallDrinkAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.drink_cardview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new SmallDrinkAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmallDrinkAdapter.SmallDrinkAdapterViewHolder holder, int position) {

        Drink drink = mDrinkData.get(position);
        holder.itemView.setTag(drink.getIdDrink());

        Glide.with(holder.mDrinkImageView.getContext()).load(drink.getStrDrinkThumb()).into(holder.mDrinkImageView);
        holder.mDrinkTextView.setText(drink.getStrDrink());
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

    public class SmallDrinkAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mDrinkTextView;
        public final ImageView mDrinkImageView;
        public SmallDrinkAdapterViewHolder(View itemView) {
            super(itemView);
            mDrinkImageView = (ImageView) itemView.findViewById(R.id.cardview_image);
            mDrinkTextView = (TextView) itemView.findViewById(R.id.cardview_drinkname);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            Drink drink = mDrinkData.get(adapterPosition);

            mClickHandler.onClick(drink.getIdDrink());
        }
    }
}
