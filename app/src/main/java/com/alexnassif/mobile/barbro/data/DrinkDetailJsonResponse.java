package com.alexnassif.mobile.barbro.data;

import java.util.List;

public class DrinkDetailJsonResponse {

    private List<Drink> drinks;

    public DrinkDetailJsonResponse(List<Drink> drinks){
        this.drinks = drinks;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }
}
