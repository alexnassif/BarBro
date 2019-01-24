package com.alexnassif.mobile.barbro.ViewModel;


import android.util.Log;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.data.DrinkListJsonResponse;
import com.google.android.gms.common.api.Api;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinksViewModel extends ViewModel {
    private LiveData<List<DrinkList>> drinks;
    private final DrinkRepository mRepository;

    public DrinksViewModel(DrinkRepository repository) {
        this.mRepository = repository;

        drinks = mRepository.loadDrinks();
    }

    public LiveData<List<DrinkList>> getDrinks() {
        return drinks;
    }
}
