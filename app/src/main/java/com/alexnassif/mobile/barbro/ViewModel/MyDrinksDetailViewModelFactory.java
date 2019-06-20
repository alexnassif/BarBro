package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyDrinksDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DrinkRepository mRepository;

    public MyDrinksDetailViewModelFactory(DrinkRepository repository){
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MyDrinksDetailViewModel(mRepository);
    }
}
