package com.alexnassif.mobile.barbro.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class DrinkList {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String strDrink;
    private String strDrinkThumb;
    private String idDrink;

    public DrinkList(int id, String strDrink, String strDrinkThumb, String idDrink){
        this.id = id;
        this.strDrink = strDrink;
        this.strDrinkThumb = strDrinkThumb;
        this.idDrink = idDrink;
    }

    @Ignore
    public DrinkList(String strDrink, String strDrinkThumb, String idDrink){
        this.id = id;
        this.strDrink = strDrink;
        this.strDrinkThumb = strDrinkThumb;
        this.idDrink = idDrink;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getStrDrink() {
        return strDrink;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public String getIdDrink() {
        return idDrink;
    }
    @Override
    public String toString(){
        return strDrink;
    }

}
