package com.alexnassif.mobile.barbro.ViewModel;

import android.util.Log;

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
    private MutableLiveData<Drink> drink;
    private int drinkId;

    public LiveData<Drink> getDrinks(int drinkId) {
        this.drinkId = drinkId;
        if(drink == null){
            drink = new MutableLiveData<Drink>();

        }
        loadDrink(drinkId);
        return drink;
    }

    private void loadDrink(int drinkId) {
        Log.d("fromvmnsid", drinkId + "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DrinkApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DrinkApi api = retrofit.create(DrinkApi.class);
        Call<DrinkDetailJsonResponse> call = api.getDetailedDrink(drinkId);


        call.enqueue(new Callback<DrinkDetailJsonResponse>() {
            @Override
            public void onResponse(Call<DrinkDetailJsonResponse> call, Response<DrinkDetailJsonResponse> response) {

                /* finally we are setting the list to our MutableLiveData */
                if(response.isSuccessful()){
                    drink.setValue(response.body().getDrinks().get(0));
                    Log.d("fromvmns", "successful");
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
