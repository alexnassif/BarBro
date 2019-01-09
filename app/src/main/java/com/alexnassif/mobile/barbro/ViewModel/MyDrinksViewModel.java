package com.alexnassif.mobile.barbro.ViewModel;

import android.app.Application;

import com.alexnassif.mobile.barbro.data.AppDatabase;
import com.alexnassif.mobile.barbro.data.MyDrink;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MyDrinksViewModel extends AndroidViewModel {

    private LiveData<List<MyDrink>> myDrinks;
    public MyDrinksViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        myDrinks = database.myDrinksDao().loadMyDrinks();
    }

    public LiveData<List<MyDrink>> getMyDrinks() {
        return myDrinks;
    }
}
