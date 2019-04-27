package com.alexnassif.mobile.barbro.utilities;

import android.content.Context;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.ViewModel.BarBroDrinksViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinkDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.DrinksViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoriteDetailViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.FavoriteViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.MyDrinksViewModelFactory;
import com.alexnassif.mobile.barbro.ViewModel.RandomViewModelFactory;
import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.BarBroDrinkApiBuilder;
import com.alexnassif.mobile.barbro.data.DrinkApiBuilder;

public class InjectorUtils {

    public static DrinkRepository provideRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        DrinkApiBuilder drinkApiBuilder = DrinkApiBuilder.getInstance();
        BarBroDrinkApiBuilder barBroDrinkApiBuilder = BarBroDrinkApiBuilder.getInstance();
        return DrinkRepository.getInstance(database.favoritesDao(), database.myDrinksDao(), executors, drinkApiBuilder, barBroDrinkApiBuilder);
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

    public static FavoriteViewModelFactory provideFavoriteViewModelFactory(Context context){
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new FavoriteViewModelFactory(repository);
    }

    public static FavoriteDetailViewModelFactory provideFavoriteDetailViewModelFactory(Context context, int drinkId){
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new FavoriteDetailViewModelFactory(repository, drinkId);
    }

    public static BarBroDrinksViewModelFactory provideBarBroDrinksVMFactory(Context context){
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new BarBroDrinksViewModelFactory(repository);
    }

    public static RandomViewModelFactory provideRandomDrinkFactory(Context context) {
        DrinkRepository repository = provideRepository(context.getApplicationContext());
        return new RandomViewModelFactory(repository);
    }
}
