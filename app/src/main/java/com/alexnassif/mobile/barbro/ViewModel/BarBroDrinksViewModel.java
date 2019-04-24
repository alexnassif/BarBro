package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.BarBroDrink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class BarBroDrinksViewModel extends ViewModel {

    private MutableLiveData<Map<String, String>> drinks;
    private LiveData<List<BarBroDrink>> drinkLV;
    private final DrinkRepository repository;

    public BarBroDrinksViewModel(final DrinkRepository repository){

        this.repository = repository;
        drinks = new MutableLiveData<Map<String, String>>();
        drinks.setValue(new HashMap<String, String>());
        drinkLV = Transformations.switchMap(drinks, new Function<Map<String, String>, LiveData<List<BarBroDrink>>>() {
            @Override
            public LiveData<List<BarBroDrink>> apply(Map<String, String> id) {
                return BarBroDrinksViewModel.this.getDrinks(id);
            }
        });

    }

    public void setDrinks(Map<String, String> drinkTypes){

        drinks.setValue(drinkTypes);

    }

    public LiveData<List<BarBroDrink>> getDrinks(Map<String, String> types){

        return repository.loadBarBroDrinks(types);
    }

    public LiveData<List<BarBroDrink>> getDrinkLV(){
        return  drinkLV;
    }
}
