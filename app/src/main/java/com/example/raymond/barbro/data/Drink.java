package com.example.raymond.barbro.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by raymond on 12/11/16.
 */

public class Drink implements Serializable {

    private String drinkName;
    private String id;
    private String ingredients;
    private String video;

    public Drink(){

    }
    public Drink(String drinkName, String ingredients, String id){
        this.drinkName = drinkName;
        this.ingredients = ingredients;
        this.id = id;
    }
    public String getVideo(){return video;}
    public String getId(){ return id; };
    public String getDrinkName(){
        return drinkName;
    }
    public String getIngredients(){
        return ingredients;
    }
    public void setVideo(String video){this.video = video; }
    public void setDrinkName(String drinkName){
        this.drinkName = drinkName;
    }
    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }
    @Override
    public String toString(){
        return drinkName;
    }

}
