package com.alexnassif.mobile.barbro;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexnassif.mobile.barbro.data.MyDrink;
import com.bumptech.glide.Glide;
import com.alexnassif.mobile.barbro.data.BarBroContract;
import com.alexnassif.mobile.barbro.data.Drink;

import java.io.File;
import java.util.List;


public class MyDrinkAdapter extends RecyclerView.Adapter<MyDrinkAdapter.MyDrinkAdapterViewHolder> {
    private List<MyDrink> mDrinkData;
    private Context context;
    private final MyDrinkAdapter.MyDrinkAdapterOnClickHandler mClickHandler;

    public MyDrinkAdapter(Context context, MyDrinkAdapter.MyDrinkAdapterOnClickHandler handler) {
        mClickHandler = handler;
        this.context = context;
    }

    public interface MyDrinkAdapterOnClickHandler{
        void onClick(MyDrink mydrink);
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

        MyDrink mydrink = mDrinkData.get(position);

        holder.mDrinkTextView.setText(mydrink.getName());

        if(mydrink.getPic() != null){
            Uri takenPhotoUri = Uri.fromFile(new File(mydrink.getPic()));
            Glide.with(holder.mDrinkImage.getContext()).load(takenPhotoUri.getPath()).centerCrop().into(holder.mDrinkImage);
        }
        holder.itemView.setTag(mydrink);
    }

    @Override
    public int getItemCount() {
        if(mDrinkData == null)
            return 0;
        return mDrinkData.size();
    }

    void swapCursor(List<MyDrink> drinkData) {
        mDrinkData = drinkData;
        notifyDataSetChanged();
    }

    public List<MyDrink> getmDrinkData() {
        return mDrinkData;
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
            MyDrink mydrink = mDrinkData.get(adapterPosition);
            mClickHandler.onClick(mydrink);

        }
    }


}
