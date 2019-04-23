package com.alexnassif.mobile.barbro.data;


import com.alexnassif.mobile.barbro.Networking.BarBroApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarBroDrinkApiBuilder {

    private static final Object LOCK = new Object();
    private static BarBroDrinkApiBuilder sInstance;

    private final BarBroApi mBarBroDrinkApi;

    private BarBroDrinkApiBuilder(BarBroApi drinkApi){
        mBarBroDrinkApi = drinkApi;
    }

    public static BarBroDrinkApiBuilder getInstance(){

        if(sInstance == null){

            synchronized (LOCK){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BarBroApi.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                sInstance = new BarBroDrinkApiBuilder(retrofit.create(BarBroApi.class));

            }
        }

        return sInstance;
    }

    public BarBroApi getmBarBroDrinkApi() {
        return mBarBroDrinkApi;
    }
}
