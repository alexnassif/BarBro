package com.alexnassif.mobile.barbro.Networking;

import com.alexnassif.mobile.barbro.BuildConfig;
import com.alexnassif.mobile.barbro.data.BarBroDrink;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BarBroApi {

    String BASE_URL = "https://www.barbroapp.com/";

    @Headers("Authorization: Api-Key " + BuildConfig.BARBRO_API_KEY)
    @GET("drinks")
    Call<List<BarBroDrink>> getDrinks(@QueryMap Map<String, String> filters);


}
