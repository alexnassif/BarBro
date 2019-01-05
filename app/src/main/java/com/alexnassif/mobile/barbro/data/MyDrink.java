package com.alexnassif.mobile.barbro.data;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mydrinks")
public class MyDrink implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String ingredients;
    private String pic;

    public MyDrink(String pic, String name, String ingredients, int id){
        this.name = name;
        this.ingredients = ingredients;
        this.id = id;
        this.pic = pic;
    }

    public int getId(){ return id; };
    public String getName(){
        return name;
    }
    public String getIngredients(){
        return ingredients;
    }
    public void setId(int id){ this.id = id; }
    public void setName(String drinkName){
        this.name = drinkName;
    }
    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString(){
        return name;
    }
}
