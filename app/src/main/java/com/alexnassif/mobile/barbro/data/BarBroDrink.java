package com.alexnassif.mobile.barbro.data;

import java.io.Serializable;
import java.util.List;

public class BarBroDrink implements Serializable {

    private int id;
    private String drinkName;
    private String glass;
    private String instructions;
    private String imageLocation;
    private boolean liqueur;
    private boolean brandy;
    private boolean whisky;
    private boolean tequila;
    private boolean gin;
    private boolean vodka;
    private boolean newDrink;
    private boolean classic;
    private boolean party;
    private boolean sour;
    private boolean fruity;
    private boolean sweet;
    private boolean popular;

    private List<String> measurements;
    private List<String> ingredients;

    public BarBroDrink(int id, String drinkName, String glass, String instructions, String imageLocation, boolean liqueur, boolean brandy, boolean whisky, boolean tequila, boolean gin, boolean vodka, boolean newDrink, boolean classic, boolean party, boolean sour, boolean fruity, boolean sweet, boolean popular, List<String> measurements, List<String> ingredients) {
        this.id = id;
        this.drinkName = drinkName;
        this.glass = glass;
        this.instructions = instructions;
        this.imageLocation = imageLocation;
        this.liqueur = liqueur;
        this.brandy = brandy;
        this.whisky = whisky;
        this.tequila = tequila;
        this.gin = gin;
        this.vodka = vodka;
        this.newDrink = newDrink;
        this.classic = classic;
        this.party = party;
        this.sour = sour;
        this.fruity = fruity;
        this.sweet = sweet;
        this.popular = popular;
        this.measurements = measurements;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public boolean isLiqueur() {
        return liqueur;
    }

    public void setLiqueur(boolean liqueur) {
        this.liqueur = liqueur;
    }

    public boolean isBrandy() {
        return brandy;
    }

    public void setBrandy(boolean brandy) {
        this.brandy = brandy;
    }

    public boolean isWhisky() {
        return whisky;
    }

    public void setWhisky(boolean whisky) {
        this.whisky = whisky;
    }

    public boolean isTequila() {
        return tequila;
    }

    public void setTequila(boolean tequila) {
        this.tequila = tequila;
    }

    public boolean isGin() {
        return gin;
    }

    public void setGin(boolean gin) {
        this.gin = gin;
    }

    public boolean isVodka() {
        return vodka;
    }

    public void setVodka(boolean vodka) {
        this.vodka = vodka;
    }

    public boolean isNewDrink() {
        return newDrink;
    }

    public void setNewDrink(boolean newDrink) {
        this.newDrink = newDrink;
    }

    public boolean isClassic() {
        return classic;
    }

    public void setClassic(boolean classic) {
        this.classic = classic;
    }

    public boolean isParty() {
        return party;
    }

    public void setParty(boolean party) {
        this.party = party;
    }

    public boolean isSour() {
        return sour;
    }

    public void setSour(boolean sour) {
        this.sour = sour;
    }

    public boolean isFruity() {
        return fruity;
    }

    public void setFruity(boolean fruity) {
        this.fruity = fruity;
    }

    public boolean isSweet() {
        return sweet;
    }

    public void setSweet(boolean sweet) {
        this.sweet = sweet;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public List<String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<String> measurements) {
        this.measurements = measurements;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
