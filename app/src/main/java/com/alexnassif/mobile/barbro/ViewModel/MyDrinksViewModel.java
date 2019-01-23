package com.alexnassif.mobile.barbro.ViewModel;

import android.app.Application;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.MyDrink;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
