package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.DrinkList;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FavoritesViewModel extends ViewModel {

    private LiveData<List<DrinkList>> favorites;
    private final DrinkRepository mRepository;

    public FavoritesViewModel(DrinkRepository repository) {

        mRepository = repository;
        favorites = mRepository.loadFavorites();
    }

    public LiveData<List<DrinkList>> getFavorites() {
        return favorites;
    }


}
