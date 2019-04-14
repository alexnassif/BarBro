package com.alexnassif.mobile.barbro.data;

import java.util.List;

public class BarBroApiResponse {

    private List<BarBroDrink> list;

    public BarBroApiResponse(List<BarBroDrink> list){

        this.list = list;

    }

    public List<BarBroDrink> getList(){
        return this.list;
    }
}
