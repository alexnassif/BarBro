package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkDetailJsonResponse;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
