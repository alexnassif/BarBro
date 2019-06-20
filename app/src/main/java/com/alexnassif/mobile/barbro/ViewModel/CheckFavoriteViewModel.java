package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;
import com.alexnassif.mobile.barbro.data.DrinkList;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class CheckFavoriteViewModel extends ViewModel {

    private MutableLiveData<Integer> drink = new MutableLiveData<Integer>();
    private LiveData<DrinkList> drinkLV;
    private DrinkRepository mRepository;

    public CheckFavoriteViewModel(DrinkRepository mRepository) {

        this.mRepository = mRepository;
        drinkLV = Transformations.switchMap(drink, new Function<Integer, LiveData<DrinkList>>() {
            @Override
            public LiveData<DrinkList> apply(Integer input) {
                return CheckFavoriteViewModel.this.getDrink(input);
            }
        });
    }

    public LiveData<DrinkList> getDrink(Integer drinkId) {

        return mRepository.getDrink(drinkId);
    }

    public void setDrink(int drinkId) {
        drink.setValue(drinkId);
    }

    public LiveData<DrinkList> checkDrinkFave(){
        return drinkLV;
    }
}
