package com.alexnassif.mobile.barbro.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.alexnassif.mobile.barbro.model.Drink;

import java.util.List;

@Dao
public interface DrinkDao {

    @Query("SELECT * FROM drinks")
    List<Drink> loadAllDrinks();


}
