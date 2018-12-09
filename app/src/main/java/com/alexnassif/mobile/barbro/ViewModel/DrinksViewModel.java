package com.alexnassif.mobile.barbro.ViewModel;


import android.util.Log;

import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;
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
    private MutableLiveData<List<DrinkList>> drinks;

    public LiveData<List<DrinkList>> getDrinks() {
        if(drinks == null){
            drinks = new MutableLiveData<List<DrinkList>>();
            loadDrinks();
        }
        return drinks;
    }

    private void loadDrinks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DrinkApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DrinkApi api = retrofit.create(DrinkApi.class);
        Call<List<DrinkList>> call = api.getDrinks();


        call.enqueue(new Callback<List<DrinkList>>() {
            @Override
            public void onResponse(Call<List<DrinkList>> call, Response<List<DrinkList>> response) {

                //finally we are setting the list to our MutableLiveData
                drinks.setValue(response.body());



            }

            @Override
            public void onFailure(Call<List<DrinkList>> call, Throwable t) {
                Log.d("fromvm", "im in results fragment");
            }

        });
    }
}
