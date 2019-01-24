package com.alexnassif.mobile.barbro.utilities;

import android.content.Context;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.MyDrinksViewModelFactory;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;

public class InjectorUtils {

    public static DrinkRepository provideRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return DrinkRepository.getInstance(database.favoritesDao(), database.myDrinksDao(), executors);
    }

    public static MyDrinksViewModelFactory provideMyDrinksViewModelFactory(Context context){
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new MyDrinksViewModelFactory(repository);
    }

    public static DrinksViewModelFactory provideDrinksViewModelFactory(Context context){
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new DrinksViewModelFactory(repository);
    }

    public static DrinkDetailViewModelFactory provideDrinkDetailViewModelFactory(Context context){
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new DrinkDetailViewModelFactory(repository);
    }
}
