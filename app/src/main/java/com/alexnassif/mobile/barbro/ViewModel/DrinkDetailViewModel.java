package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

import com.alexnassif.mobile.barbro.DrinkRepository;
import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkDetailJsonResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinkDetailViewModel extends ViewModel {

    private LiveData<Drink> drink = new MutableLiveData<Drink>();
    private DrinkRepository mRepository;

    public DrinkDetailViewModel(DrinkRepository mRepository) {
        this.mRepository = mRepository;
    }

    public LiveData<Drink> getDrink() {

        return drink;
    }

    public void setDrink(int drinkId) {
        drink = mRepository.loadDrink(drinkId);
    }
}
