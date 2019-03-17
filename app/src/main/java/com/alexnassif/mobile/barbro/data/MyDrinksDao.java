package com.alexnassif.mobile.barbro.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MyDrinksDao {
    @Query("SELECT * FROM mydrinks")
    LiveData<List<MyDrink>> loadMyDrinks();

    @Query("SELECT * FROM mydrinks WHERE id = :id")
    LiveData<MyDrink> loadMyDrinkById(int id);

    @Insert
    void insertMyDrink(MyDrink drink);

    @Update
    void updateMyDrink(MyDrink drink);

    @Delete
    void deleteMyDrink(MyDrink drink);
}
