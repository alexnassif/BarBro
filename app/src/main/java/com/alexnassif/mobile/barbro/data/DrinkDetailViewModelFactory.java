package com.alexnassif.mobile.barbro.data;

import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class DrinkDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int mDrinkId;

    public DrinkDetailViewModelFactory(int drinkId){

        mDrinkId = drinkId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DrinkDetailViewModel();
    }
}
