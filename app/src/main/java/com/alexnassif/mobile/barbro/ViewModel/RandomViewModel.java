package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkDetailJsonResponse;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomViewModel extends ViewModel {
    private MutableLiveData<List<Drink>> drink;

    public LiveData<List<Drink>> getDrinks() {
        if(drink == null){
            drink = new MutableLiveData<List<Drink>>();
            loadRandomDrink();
        }
        return drink;
    }

    public void loadRandomDrink() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DrinkApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DrinkApi api = retrofit.create(DrinkApi.class);
        Call<DrinkDetailJsonResponse> call = api.getRandomDrink();


        call.enqueue(new Callback<DrinkDetailJsonResponse>() {
            @Override
            public void onResponse(Call<DrinkDetailJsonResponse> call, Response<DrinkDetailJsonResponse> response) {

                /* finally we are setting the list to our MutableLiveData */
                if(response.isSuccessful()){
                    drink.setValue(response.body().getDrinks());
                }
                else{
                    Log.d("fromvmns", "not successful");
                }



            }

            @Override
            public void onFailure(Call<DrinkDetailJsonResponse> call, Throwable t) {
                Log.d("fromvmerror", t.getMessage());
            }

        });
    }
}
