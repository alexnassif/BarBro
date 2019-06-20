package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.Drink;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RandomViewModel extends ViewModel {
    private MutableLiveData<List<Drink>> drink;
    private LiveData<String> drinkRV;
    private DrinkRepository repository;

    public RandomViewModel(DrinkRepository repository) {
        this.repository = repository;
        drink = this.repository.loadRandomDrink();
    }

    public LiveData<List<Drink>> getDrinks() {

        return drink;

    }

    public void loadRandomDrink() {
        repository.loadRandomDrinkList(this.drink);

    }
}
