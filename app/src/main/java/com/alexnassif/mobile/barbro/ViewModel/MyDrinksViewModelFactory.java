package com.alexnassif.mobile.barbro.ViewModel;

import com.alexnassif.mobile.barbro.DrinkRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyDrinksViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DrinkRepository mRepository;

    public MyDrinksViewModelFactory(DrinkRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new MyDrinksViewModel(mRepository);
    }
}
