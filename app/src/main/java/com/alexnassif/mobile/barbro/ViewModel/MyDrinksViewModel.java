package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.MyDrink;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MyDrinksViewModel extends ViewModel {

    private final LiveData<List<MyDrink>> myDrinks;
    private final DrinkRepository mRepository;

    public MyDrinksViewModel(DrinkRepository repository) {
        mRepository = repository;

        myDrinks = mRepository.getMyDrinks();
    }

    public LiveData<List<MyDrink>> getMyDrinks() {
        return myDrinks;
    }
}
