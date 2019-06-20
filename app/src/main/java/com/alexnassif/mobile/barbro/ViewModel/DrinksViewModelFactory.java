package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.data.DrinkRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DrinksViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final DrinkRepository mRepository;

    public DrinksViewModelFactory(DrinkRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new DrinksViewModel(mRepository);
    }
}
