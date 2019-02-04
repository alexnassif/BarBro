package com.alexnassif.mobile.barbro.data;

import com.alexnassif.mobile.barbro.Networking.DrinkApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinkApiBuilder {

    private static final Object LOCK = new Object();
    private static DrinkApiBuilder sInstance;

    private final DrinkApi mDrinkApi;

    private DrinkApiBuilder(DrinkApi drinkApi){
        mDrinkApi = drinkApi;
    }

    public static DrinkApiBuilder getInstance(){

        if(sInstance == null){

            synchronized (LOCK){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(DrinkApi.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                sInstance = new DrinkApiBuilder(retrofit.create(DrinkApi.class));

            }
        }

        return sInstance;
    }

    public DrinkApi getmDrinkApi() {
        return mDrinkApi;
    }
}
