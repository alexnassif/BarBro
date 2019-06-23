package com.alexnassif.mobile.barbro.data;

import android.util.Log;

import com.alexnassif.mobile.barbro.Networking.FavoritesDao;
import com.alexnassif.mobile.barbro.Networking.MyDrinksDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkRepository {

    private static final Object LOCK = new Object();
    private static DrinkRepository instance;
    private final FavoritesDao mFavoritesDao;
    private final MyDrinksDao mDrinksDao;
    private final AppExecutors mExecutors;
    private final DrinkApiBuilder mDrinkApi;
    private final BarBroDrinkApiBuilder mBarBroDrinkApi;
    private boolean mInitialized = false;

    private DrinkRepository(FavoritesDao mFavoritesDao, MyDrinksDao mDrinksDao, AppExecutors mExecutors, DrinkApiBuilder drinkApi, BarBroDrinkApiBuilder barBroDrinkApi) {
        this.mFavoritesDao = mFavoritesDao;
        this.mDrinksDao = mDrinksDao;
        this.mExecutors = mExecutors;
        this.mDrinkApi = drinkApi;
        this.mBarBroDrinkApi = barBroDrinkApi;
    }

    public synchronized static DrinkRepository getInstance(FavoritesDao mFavoritesDao, MyDrinksDao mDrinksDao, AppExecutors mExecutors, DrinkApiBuilder drinkApi, BarBroDrinkApiBuilder barBroDrinkApi){

        if(instance == null){
            synchronized (LOCK){
                instance = new DrinkRepository(mFavoritesDao, mDrinksDao, mExecutors, drinkApi, barBroDrinkApi);
            }
        }

        return instance;
    }

    public LiveData<List<MyDrink>> getMyDrinks(){
        return mDrinksDao.loadMyDrinks();
    }

    public LiveData<MyDrink> getMyDrink(int id) {
        return mDrinksDao.loadMyDrinkById(id);
    }

    public LiveData<List<DrinkList>> loadFavorites(){
        return mFavoritesDao.loadFavorites();
    }

    public LiveData<DrinkList> getDrink(int id){
        return mFavoritesDao.loadFavoriteById(id);
    }

    public LiveData<List<DrinkList>> loadDrinks() {

        final MutableLiveData<List<DrinkList>> drinks = new MutableLiveData<List<DrinkList>>();


        Call<DrinkListJsonResponse> call = mDrinkApi.getmDrinkApi().getDrinks();

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
        Call<DrinkDetailJsonResponse> call = mDrinkApi.getmDrinkApi().getDetailedDrink(drinkId);


        call.enqueue(new Callback<DrinkDetailJsonResponse>() {
            @Override
            public void onResponse(Call<DrinkDetailJsonResponse> call, Response<DrinkDetailJsonResponse> response) {

                /* finally we are setting the list to our MutableLiveData */
                if (response.isSuccessful()) {
                    drink.setValue(response.body().getDrinks().get(0));
                    Log.d("drinkvm", response.body().getDrinks().get(0).getStrDrink());
                } else {
                }


            }

            @Override
            public void onFailure(Call<DrinkDetailJsonResponse> call, Throwable t) {

            }

        });
        return drink;
    }

    public MutableLiveData<List<Drink>> loadRandomDrink(){
        final MutableLiveData<List<Drink>> drink = new MutableLiveData<List<Drink>>();

        Call<DrinkDetailJsonResponse> call = mDrinkApi.getmDrinkApi().getRandomDrink();


        call.enqueue(new Callback<DrinkDetailJsonResponse>() {
            @Override
            public void onResponse(Call<DrinkDetailJsonResponse> call, Response<DrinkDetailJsonResponse> response) {

                /* finally we are setting the list to our MutableLiveData */
                if(response.isSuccessful()){
                    drink.postValue(response.body().getDrinks());
                    Log.d("randomviewvalue", response.body().getDrinks().get(0).getStrDrink());
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

        return drink;
    }

    public MutableLiveData<List<BarBroDrink>> loadAllBarBroDrinks() {

        final MutableLiveData<List<BarBroDrink>> drinks = new MutableLiveData<List<BarBroDrink>>();
        Map<String, String> types = new HashMap<String, String>();
        Call<List<BarBroDrink>> bbCall = mBarBroDrinkApi.getmBarBroDrinkApi().getDrinks(types);
        bbCall.enqueue(new Callback<List<BarBroDrink>>() {
            @Override
            public void onResponse(Call<List<BarBroDrink>> call, Response<List<BarBroDrink>> response) {

                if (response.isSuccessful()) {

                    drinks.setValue(response.body());
                } else {
                }

            }

            @Override
            public void onFailure(Call<List<BarBroDrink>> call, Throwable t) {
            }
        });

        return drinks;

    }

    public LiveData<List<BarBroDrink>> loadBarBroDrinks(Map <String, String> types) {

        final MutableLiveData<List<BarBroDrink>> drinks = new MutableLiveData<List<BarBroDrink>>();

        Call<List<BarBroDrink>> bbCall = mBarBroDrinkApi.getmBarBroDrinkApi().getDrinks(types);
        bbCall.enqueue(new Callback<List<BarBroDrink>>() {
            @Override
            public void onResponse(Call<List<BarBroDrink>> call, Response<List<BarBroDrink>> response) {

                if(response.isSuccessful()){

                    drinks.setValue(response.body());
                }
                else{
                }

            }

            @Override
            public void onFailure(Call<List<BarBroDrink>> call, Throwable t) {
                Log.d("bblisterror1", t.getMessage());
            }
        });

        return drinks;
    }

    public void loadRandomDrinkList(final MutableLiveData<List<Drink>> drink) {

        Call<DrinkDetailJsonResponse> call = mDrinkApi.getmDrinkApi().getRandomDrink();


        call.enqueue(new Callback<DrinkDetailJsonResponse>() {
            @Override
            public void onResponse(Call<DrinkDetailJsonResponse> call, Response<DrinkDetailJsonResponse> response) {

                /* finally we are setting the list to our MutableLiveData */
                if(response.isSuccessful()){
                    drink.postValue(response.body().getDrinks());
                    Log.d("randomviewvalue", response.body().getDrinks().get(0).getStrDrink());
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