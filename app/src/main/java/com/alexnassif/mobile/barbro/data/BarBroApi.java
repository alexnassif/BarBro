package com.alexnassif.mobile.barbro.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface BarBroApi {

    String BASE_URL = "http://www.barbroapp.com/";

    @Headers("Authorization: Api-Key 1234567")
    @GET("drinks")
    Call<List<BarBroDrink>> getDrinks();
}
