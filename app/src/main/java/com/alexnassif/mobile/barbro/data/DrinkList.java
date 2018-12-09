package com.alexnassif.mobile.barbro.data;

public class DrinkList {

    private String strDrink;
    private String strDrinkThumb;
    private String idDrink;

    public DrinkList(String strDrink, String strDrinkThumb, String idDrink){
        this.strDrink = strDrink;
        this.strDrinkThumb = strDrinkThumb;
        this.idDrink = idDrink;
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

}
