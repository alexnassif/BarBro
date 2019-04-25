package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.data.BarBroDrink;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BarBroDrinksViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DrinkRepository mRepository;

    public BarBroDrinksViewModelFactory(DrinkRepository mRepository) {
        this.mRepository = mRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new BarBroDrinksViewModel(mRepository);
    }
}
