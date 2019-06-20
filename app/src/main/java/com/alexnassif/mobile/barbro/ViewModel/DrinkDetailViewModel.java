package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.Drink;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class DrinkDetailViewModel extends ViewModel {

    private MutableLiveData<Integer> drink = new MutableLiveData<Integer>();
    private LiveData<Drink> drinkLV;
    private DrinkRepository mRepository;

    public DrinkDetailViewModel(DrinkRepository mRepository) {

        this.mRepository = mRepository;
        drinkLV = Transformations.switchMap(drink, new Function<Integer, LiveData<Drink>>() {
            @Override
            public LiveData<Drink> apply(Integer input) {
                return DrinkDetailViewModel.this.getDrink(input);
            }
        });
    }

    public LiveData<Drink> getDrink(Integer drinkId) {

        return mRepository.loadDrink(drinkId);
    }

    public void setDrink(int drinkId) {
         drink.setValue(drinkId);
    }

    public LiveData<Drink> getDrinkLV(){
        return drinkLV;
    }
}
