package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.MyDrink;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyDrinksDetailViewModel extends ViewModel {

    private LiveData<MyDrink> drink = new MutableLiveData<MyDrink>();
    private DrinkRepository mRepository;

    public MyDrinksDetailViewModel(DrinkRepository mRepository) {
        this.mRepository = mRepository;
    }

    public LiveData<MyDrink> getDrink() {

        return drink;
    }

    public void setDrink(int drinkId) {
        drink = mRepository.getMyDrink(drinkId);
    }
}
