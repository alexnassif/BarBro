package com.alexnassif.mobile.barbro.data;

import java.io.Serializable;

/**
 * Created by mobile on 12/11/16.
 */

public class Drink implements Serializable {

    private int dbId;
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
    public void setId(String id){ this.id = id; }
    public void setVideo(String video){this.video = video; }
    public void setDrinkName(String drinkName){
        this.drinkName = drinkName;
    }
    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }
    public void setDbId(int id){
        this.dbId = id;
    }
    public int getDbId(){return dbId;}
    @Override
    public String toString(){
        return drinkName;
    }

}
