package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.DrinkRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FavoriteDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DrinkRepository mRepository;
    private int drinkId;

    public FavoriteDetailViewModelFactory(DrinkRepository repository, int drinkId){
        this.mRepository = repository;
        this.drinkId = drinkId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoritesDetailViewModel(mRepository, this.drinkId);
    }
}
