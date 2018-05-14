package com.alexnassif.mobile.barbro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by mobile on 12/11/16.
 */

@Entity(tableName = "drinks")
public class Drink implements Serializable {


    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int _id;

    private String drinkName;
    private String pic;
    private String ingredients;
    private String video;
    private String description;
    private int berry;
    private int fresh;
    private int fruity;
    private int herb;
    private int sour;
    private int spicy;
    private int sweet;
    private int favorite;
    private int vodka;
    private int gin;
    private int rum;
    private int tequila;
    private int whisky;
    private int brandy;


    public Drink() {

    }

    @Ignore
    public Drink(String drinkName, String ingredients, String id) {
        this.drinkName = drinkName;
        this.ingredients = ingredients;
        this.pic = id;
    }

    public String getVideo() {
        return video;
    }

    public String getPic() {
        return pic;
    }

    ;

    public String getDrinkName() {
        return drinkName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public int get_id() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBerry() {
        return berry;
    }

    public void setBerry(int berry) {
        this.berry = berry;
    }

    public int getFresh() {
        return fresh;
    }

    public void setFresh(int fresh) {
        this.fresh = fresh;
    }

    public int getFruity() {
        return fruity;
    }

    public void setFruity(int fruity) {
        this.fruity = fruity;
    }

    public int getHerb() {
        return herb;
    }

    public void setHerb(int herb) {
        this.herb = herb;
    }

    public int getSour() {
        return sour;
    }

    public void setSour(int sour) {
        this.sour = sour;
    }

    public int getSpicy() {
        return spicy;
    }

    public void setSpicy(int spicy) {
        this.spicy = spicy;
    }

    public int getSweet() {
        return sweet;
    }

    public void setSweet(int sweet) {
        this.sweet = sweet;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getVodka() {
        return vodka;
    }

    public void setVodka(int vodka) {
        this.vodka = vodka;
    }

    public int getGin() {
        return gin;
    }

    public void setGin(int gin) {
        this.gin = gin;
    }

    public int getRum() {
        return rum;
    }

    public void setRum(int rum) {
        this.rum = rum;
    }

    public int getTequila() {
        return tequila;
    }

    public void setTequila(int tequila) {
        this.tequila = tequila;
    }

    public int getWhisky() {
        return whisky;
    }

    public void setWhisky(int whisky) {
        this.whisky = whisky;
    }

    public int getBrandy() {
        return brandy;
    }

    public void setBrandy(int brandy) {
        this.brandy = brandy;
    }

    @Override
    public String toString() {
        return drinkName;
    }

}
