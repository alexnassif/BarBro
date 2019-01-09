package com.alexnassif.mobile.barbro.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MyDrinksDao {
    @Query("SELECT * FROM mydrinks")
    LiveData<List<MyDrink>> loadMyDrinks();

    @Insert
    void insertMyDrink(MyDrink drink);

    @Delete
    void deleteMyDrink(MyDrink drink);
}
