package com.alexnassif.mobile.barbro.Adapters;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngMeaAdapter extends RecyclerView.Adapter<IngMeaAdapter.IngMeaAdapterHolder> {

    private List<String> ingredients;
    private List<String> measurements;

    @NonNull
    @Override
    public IngMeaAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull IngMeaAdapterHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class IngMeaAdapterHolder extends RecyclerView.ViewHolder {
        public IngMeaAdapterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
