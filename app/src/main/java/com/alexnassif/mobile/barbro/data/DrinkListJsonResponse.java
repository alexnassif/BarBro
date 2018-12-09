package com.alexnassif.mobile.barbro.data;

import java.util.List;

public class DrinkListJsonResponse {

    private List<DrinkList> drinks;
    public DrinkListJsonResponse(List<DrinkList> list){
        this.drinks = list;

    }
    public List<DrinkList> getList() {
        return drinks;
    }

    public void setList(List<DrinkList> list) {
        this.drinks = list;
    }


}
