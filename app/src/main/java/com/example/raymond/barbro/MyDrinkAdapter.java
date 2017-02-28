package com.example.raymond.barbro;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;

import java.io.File;


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
        int drinkIngredients = mDrinkData.getColumnIndexOrThrow(BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS);
        int drinkPic = mDrinkData.getColumnIndex(BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC);

        mDrinkData.moveToPosition(position);

        final int id = mDrinkData.getInt(drinkId);
        final String stringID = Integer.toString(id);
        final String _drinkName = mDrinkData.getString(drinkName);
        final String _drinkIngredients = mDrinkData.getString(drinkIngredients);
        final String _drinkPic = mDrinkData.getString(drinkPic);

        final Drink drink = new Drink();
        drink.setDbId(id);
        drink.setDrinkName(_drinkName);
        drink.setIngredients(_drinkIngredients);
        drink.setId(_drinkPic);
        if (_drinkPic != null) {
            Uri takenPhotoUri = Uri.fromFile(new File(_drinkPic));
            Glide.with(holder.mDrinkImage.getContext()).load(takenPhotoUri.getPath()).centerCrop().into(holder.mDrinkImage);
        }
        holder.itemView.setTag(drink);
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
        public final ImageView mDrinkImage;
        public MyDrinkAdapterViewHolder(View itemView) {
            super(itemView);
            mDrinkTextView = (TextView) itemView.findViewById(R.id.my_drink_name);
            mDrinkImage = (ImageView) itemView.findViewById(R.id.my_drink_pic);
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
