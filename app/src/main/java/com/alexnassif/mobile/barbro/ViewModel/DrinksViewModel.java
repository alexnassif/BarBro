package com.alexnassif.mobile.barbro.ViewModel;


import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.DrinkList;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DrinksViewModel extends ViewModel {
    private LiveData<List<DrinkList>> drinks;
    private final DrinkRepository mRepository;

    public DrinksViewModel(DrinkRepository repository) {
        this.mRepository = repository;

        drinks = mRepository.loadDrinks();
    }

    public LiveData<List<DrinkList>> getDrinks() {
        return drinks;
    }
}
