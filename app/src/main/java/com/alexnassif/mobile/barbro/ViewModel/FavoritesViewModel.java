package com.alexnassif.mobile.barbro.ViewModel;

import android.app.Application;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
