package com.alexnassif.mobile.barbro.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    List<Drink> loadFavorites();

    @Insert
    void insertFavorite(FavoriteEntry favorite);

    @Delete
    void deleteFavorite(FavoriteEntry favorite);
}
