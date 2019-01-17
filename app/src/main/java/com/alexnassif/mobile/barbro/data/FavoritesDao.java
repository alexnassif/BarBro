package com.alexnassif.mobile.barbro.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<DrinkList>> loadFavorites();

    @Query("SELECT * FROM favorites WHERE idDrink = :id")
    LiveData<DrinkList> loadFavoriteById(int id);

    @Insert
    void insertFavorite(DrinkList favorite);

    @Delete
    void deleteFavorite(DrinkList favorite);
}
