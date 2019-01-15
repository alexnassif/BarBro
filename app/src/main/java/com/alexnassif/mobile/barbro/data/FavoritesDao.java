package com.alexnassif.mobile.barbro.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    List<DrinkList> loadFavorites();

    @Insert
    void insertFavorite(DrinkList favorite);

    @Delete
    void deleteFavorite(DrinkList favorite);
}
