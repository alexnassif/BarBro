package com.alexnassif.mobile.barbro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.R;
import com.alexnassif.mobile.barbro.data.BarBroDrink;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BarBroDrinkAdapter extends RecyclerView.Adapter<BarBroDrinkAdapter.BarBroDrinkHolder> {

    private List<BarBroDrink> drinks;
    private Context context;
    private final BarBroDrinkOnClickHandler mClickHandler;

    public BarBroDrinkAdapter(Context context, BarBroDrinkOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    public interface BarBroDrinkOnClickHandler{
        void onClick(BarBroDrink drink);
    }
    @NonNull
    @Override
    public BarBroDrinkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.drink_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new BarBroDrinkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarBroDrinkHolder holder, int position) {
        BarBroDrink drink = drinks.get(position);
        holder.mDrinkTextView.setText(drink.getDrinkName());

    }

    @Override
    public int getItemCount() {

        if(this.drinks == null)
            return 0;

        return this.drinks.size();
    }

    public void swapList(List<BarBroDrink> drinks){

        this.drinks = drinks;
        notifyDataSetChanged();

    }

    public class BarBroDrinkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDrinkTextView;
        private ImageView mDrinkImageView;
        public BarBroDrinkHolder(@NonNull View itemView) {
            super(itemView);

            mDrinkImageView = (ImageView) itemView.findViewById(R.id.drink_image);
            mDrinkTextView = (TextView) itemView.findViewById(R.id.drink_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
