package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;

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
