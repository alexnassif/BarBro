package com.alexnassif.mobile.barbro;

import android.util.Log;

import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkDetailJsonResponse;
import com.alexnassif.mobile.barbro.data.DrinkList;
import com.alexnassif.mobile.barbro.data.DrinkListJsonResponse;
import com.alexnassif.mobile.barbro.data.FavoritesDao;
import com.alexnassif.mobile.barbro.data.MyDrink;
import com.alexnassif.mobile.barbro.data.MyDrinksDao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinkRepository {

    private static final Object LOCK = new Object();
    private static DrinkRepository instance;
    private final FavoritesDao mFavoritesDao;
    private final MyDrinksDao mDrinksDao;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    public DrinkRepository(FavoritesDao mFavoritesDao, MyDrinksDao mDrinksDao, AppExecutors mExecutors) {
        this.mFavoritesDao = mFavoritesDao;
        this.mDrinksDao = mDrinksDao;
        this.mExecutors = mExecutors;
    }

    public synchronized static DrinkRepository getInstance(FavoritesDao mFavoritesDao, MyDrinksDao mDrinksDao, AppExecutors mExecutors){

        if(instance == null){
            synchronized (LOCK){
                instance = new DrinkRepository(mFavoritesDao, mDrinksDao, mExecutors);
            }
        }

        return instance;
    }

    public LiveData<List<MyDrink>> getMyDrinks(){
        return mDrinksDao.loadMyDrinks();
    }

    public LiveData<List<DrinkList>> loadFavorites(){
        return mFavoritesDao.loadFavorites();
    }

    public LiveData<DrinkList> getDrink(int id){
        return mFavoritesDao.loadFavoriteById(id);
    }

    public LiveData<List<DrinkList>> loadDrinks() {

        final MutableLiveData<List<DrinkList>> drinks = new MutableLiveData<List<DrinkList>>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DrinkApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DrinkApi api = retrofit.create(DrinkApi.class);
        Call<DrinkListJsonResponse> call = api.getDrinks();


        call.enqueue(new Callback<DrinkListJsonResponse>() {
            @Override
            public void onResponse(Call<DrinkListJsonResponse> call, Response<DrinkListJsonResponse> response) {

                /* finally we are setting the list to our MutableLiveData */
                if(response.isSuccessful()){
                    drinks.setValue(response.body().getList());
                }
                else{
                    Log.d("fromvmns", "not successful");
                }



            }

            @Override
            public void onFailure(Call<DrinkListJsonResponse> call, Throwable t) {
                Log.d("fromvmerror", t.getMessage());
            }

        });

        return drinks;
    }

    public LiveData<Drink> loadDrink(int drinkId) {
        final MutableLiveData<Drink> drink = new MutableLiveData<Drink>();
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
                if (response.isSuccessful()) {
                    drink.setValue(response.body().getDrinks().get(0));
                    Log.d("fromvmns", "successful");
                } else {
                    Log.d("fromvmns", "not successful");
                }


            }

            @Override
            public void onFailure(Call<DrinkDetailJsonResponse> call, Throwable t) {
                Log.d("fromvmerror", t.getMessage());
            }

        });
        return drink;
    }
}
