package com.example.raymond.barbro;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;


public class MyDrinkAdapter extends RecyclerView.Adapter<MyDrinkAdapter.MyDrinkAdapterViewHolder> {
    private Cursor mDrinkData;
    private Context context;
    private final MyDrinkAdapter.MyDrinkAdapterOnClickHandler mClickHandler;

    public MyDrinkAdapter(Context context, MyDrinkAdapter.MyDrinkAdapterOnClickHandler handler) {
        mClickHandler = handler;
        this.context = context;
    }

    public interface MyDrinkAdapterOnClickHandler{
        void onClick(Drink drink);
    }

    @Override
    public MyDrinkAdapter.MyDrinkAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.mydrink_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MyDrinkAdapter.MyDrinkAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyDrinkAdapter.MyDrinkAdapterViewHolder holder, int position) {
        int drinkId = mDrinkData.getColumnIndex(BarBroContract.MyDrinkEntry._ID);
        int drinkName = mDrinkData.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);


        mDrinkData.moveToPosition(position);

        final int id = mDrinkData.getInt(drinkId);
        final String stringID = Integer.toString(id);
        String _drinkName = mDrinkData.getString(drinkName);
        holder.itemView.setTag(id);
        holder.mDrinkTextView.setText(_drinkName);

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

    public class MyDrinkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mDrinkTextView;
        public MyDrinkAdapterViewHolder(View itemView) {
            super(itemView);
            mDrinkTextView = (TextView) itemView.findViewById(R.id.my_drink_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            mDrinkData.moveToPosition(adapterPosition);
            int drinkId = mDrinkData.getColumnIndex(BarBroContract.MyDrinkEntry._ID);
            int drinkName = mDrinkData.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME);
            int ingredients = mDrinkData.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);


            int id = mDrinkData.getInt(drinkId);
            String _drinkName = mDrinkData.getString(drinkName);
            String drinkIngredients = mDrinkData.getString(ingredients);

            final String stringID = Integer.toString(id);

            Drink drink = new Drink();
            drink.setDrinkName(_drinkName);
            drink.setIngredients(drinkIngredients);
            mClickHandler.onClick(drink);

        }
    }
}
