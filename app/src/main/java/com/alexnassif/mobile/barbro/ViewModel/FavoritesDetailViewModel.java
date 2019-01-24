package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.DrinkList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FavoritesDetailViewModel extends ViewModel {

    private LiveData<DrinkList> fave;
    private final DrinkRepository mRepository;
    private int drinkId;

    public FavoritesDetailViewModel(DrinkRepository repository, int drinkId) {
        this.mRepository = repository;
        this.drinkId = drinkId;
        fave = mRepository.getDrink(this.drinkId);
    }

    public LiveData<DrinkList> getFave() {
        return fave;
    }
}
