package com.alexnassif.mobile.barbro.Networking;

import com.alexnassif.mobile.barbro.BuildConfig;
import com.alexnassif.mobile.barbro.data.DrinkDetailJsonResponse;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.data.DrinkListJsonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DrinkApi {

    String BASE_URL = "https://www.thecocktaildb.com/api/json/v2/" + BuildConfig.COCKTAIL_DB_KEY +"/";

    @GET("filter.php?c=Ordinary_Drink")
    Call<DrinkListJsonResponse> getDrinks();

    @GET("lookup.php")
    Call<DrinkDetailJsonResponse> getDetailedDrink(@Query("i") int drinkId);

    @GET("random.php")
    Call<DrinkDetailJsonResponse> getRandomDrink();
}
