package com.alexnassif.mobile.barbro.Networking;

import com.alexnassif.mobile.barbro.data.DrinkList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DrinkApi {

    String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink";

    @GET()
    Call<List<DrinkList>> getDrinks();
}
