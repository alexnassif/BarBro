package com.alexnassif.mobile.barbro.Networking;

import com.alexnassif.mobile.barbro.data.DrinkList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DrinkApi {

    String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";

    @GET("filter.php?c=Ordinary_Drink")
    Call<List<DrinkList>> getDrinks();
}
