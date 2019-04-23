package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.BarBroDrink;
import com.alexnassif.mobile.barbro.data.Drink;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarBroDrinksViewModel extends ViewModel {

    private LiveData<List<BarBroDrink>> drinks = new MutableLiveData<List<BarBroDrink>>();
    private final DrinkRepository repository;

    public BarBroDrinksViewModel(DrinkRepository repository){

        this.repository = repository;
    }

    public void setDrinks(Map<String, String> drinkTypes){
        drinks = this.repository.loadBarBroDrinks(drinkTypes);

    }

    public LiveData<List<BarBroDrink>> getDrinks(){
        return drinks;
    }
}
