package com.example.raymond.barbro.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by raymond on 12/11/16.
 */

public class Drink implements Serializable {

    private String drinkName;
    private String id;
    private ArrayList<String> ingredients;

    public Drink(){

    }
    public Drink(String drinkName, ArrayList<String> ingredients, String id){
        this.drinkName = drinkName;
        this.ingredients = ingredients;
        this.id = id;
    }
    public String getId(){ return id; };
    public String getDrinkName(){
        return drinkName;
    }
    public ArrayList<String> getIngredients(){
        return ingredients;
    }
    public void setDrinkName(String drinkName){
        this.drinkName = drinkName;
    }
    public void setIngredients(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }
    @Override
    public String toString(){
        return drinkName;
    }

}
