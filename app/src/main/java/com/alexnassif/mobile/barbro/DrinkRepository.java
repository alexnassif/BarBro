package com.alexnassif.mobile.barbro;

import com.alexnassif.mobile.barbro.Networking.DrinkApi;
import com.alexnassif.mobile.barbro.data.AppExecutors;
import com.alexnassif.mobile.barbro.data.FavoritesDao;
import com.alexnassif.mobile.barbro.data.MyDrink;
import com.alexnassif.mobile.barbro.data.MyDrinksDao;

import java.util.List;

import androidx.lifecycle.LiveData;

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
}
