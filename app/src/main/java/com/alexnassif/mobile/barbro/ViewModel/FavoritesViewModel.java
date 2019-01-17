package com.alexnassif.mobile.barbro.ViewModel;

import android.app.Application;

import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.Drink;
import com.alexnassif.mobile.barbro.data.DrinkList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavoritesViewModel extends AndroidViewModel {

    private LiveData<List<DrinkList>> favorites;
    private LiveData<DrinkList> fave;
    private AppDatabase database;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getsInstance(this.getApplication());
        favorites = database.favoritesDao().loadFavorites();
    }

    public LiveData<List<DrinkList>> getFavorites() {
        return favorites;
    }

    public  LiveData<DrinkList> getFave(int id){
        fave = database.favoritesDao().loadFavoriteById(id);
        return fave;
    }
}
