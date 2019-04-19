package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.BarBroDrink;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BarBroDrinksViewModel extends ViewModel {

    private LiveData<List<BarBroDrink>> drinks;
    private final DrinkRepository repository;

    public BarBroDrinksViewModel(DrinkRepository repository){

        this.repository = repository;
        drinks = this.repository.loadBarBroDrinks();
    }

    public LiveData<List<BarBroDrink>> getDrinks(){
        return drinks;
    }
}
