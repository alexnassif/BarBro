package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.DrinkRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CheckFavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DrinkRepository mRepository;

    public CheckFavoriteViewModelFactory(DrinkRepository repository){
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CheckFavoriteViewModel(mRepository);
    }
}
