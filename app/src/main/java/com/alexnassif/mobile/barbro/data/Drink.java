package com.alexnassif.mobile.barbro.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.text.TextUtils;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by mobile on 12/11/16.
 */

public class Drink implements Serializable {

    private int idDrink;

    private String strDrink;
    private String strCategory;
    private String strIBA;
    private String strAlcoholic;
    private String strGlass;
    private String strInstructions;
    private String strDrinkThumb;

    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;

    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;
    private String strMeasure11;
    private String strMeasure12;
    private String strMeasure13;
    private String strMeasure14;
    private String strMeasure15;

    public Drink(int id, String strDrink, int idDrink, String strCategory, String strIBA, String strAlcoholic, String strGlass, String strInstructions, String strDrinkThumb, String strIngredient1, String strIngredient2, String strIngredient3, String strIngredient4, String strIngredient5, String strIngredient6, String strIngredient7, String strIngredient8, String strIngredient9, String strIngredient10, String strIngredient11, String strIngredient12, String strIngredient13, String strIngredient14, String strIngredient15, String strMeasure1, String strMeasure2, String strMeasure3, String strMeasure4, String strMeasure5, String strMeasure6, String strMeasure7, String strMeasure8, String strMeasure9, String strMeasure10, String strMeasure11, String strMeasure12, String strMeasure13, String strMeasure14, String strMeasure15) {

        this.strDrink = strDrink;
        this.idDrink = idDrink;
        this.strCategory = strCategory;
        this.strIBA = strIBA;
        this.strAlcoholic = strAlcoholic;
        this.strGlass = strGlass;
        this.strInstructions = strInstructions;
        this.strDrinkThumb = strDrinkThumb;
        this.strIngredient1 = strIngredient1;
        this.strIngredient2 = strIngredient2;
        this.strIngredient3 = strIngredient3;
        this.strIngredient4 = strIngredient4;
        this.strIngredient5 = strIngredient5;
        this.strIngredient6 = strIngredient6;
        this.strIngredient7 = strIngredient7;
        this.strIngredient8 = strIngredient8;
        this.strIngredient9 = strIngredient9;
        this.strIngredient10 = strIngredient10;
        this.strIngredient11 = strIngredient11;
        this.strIngredient12 = strIngredient12;
        this.strIngredient13 = strIngredient13;
        this.strIngredient14 = strIngredient14;
        this.strIngredient15 = strIngredient15;
        this.strMeasure1 = strMeasure1;
        this.strMeasure2 = strMeasure2;
        this.strMeasure3 = strMeasure3;
        this.strMeasure4 = strMeasure4;
        this.strMeasure5 = strMeasure5;
        this.strMeasure6 = strMeasure6;
        this.strMeasure7 = strMeasure7;
        this.strMeasure8 = strMeasure8;
        this.strMeasure9 = strMeasure9;
        this.strMeasure10 = strMeasure10;
        this.strMeasure11 = strMeasure11;
        this.strMeasure12 = strMeasure12;
        this.strMeasure13 = strMeasure13;
        this.strMeasure14 = strMeasure14;
        this.strMeasure15 = strMeasure15;

        //this.ingredients = getAllIngredients();
        //this.measuresments = getMeasurements();
    }

    public void setIdDrink(int idDrink) {
        this.idDrink = idDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void setStrIBA(String strIBA) {
        this.strIBA = strIBA;
    }

    public void setStrAlcoholic(String strAlcoholic) {
        this.strAlcoholic = strAlcoholic;
    }

    public void setStrGlass(String strGlass) {
        this.strGlass = strGlass;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    public void setStrIngredient1(String strIngredient1) {
        this.strIngredient1 = strIngredient1;
    }

    public void setStrIngredient2(String strIngredient2) {
        this.strIngredient2 = strIngredient2;
    }

    public void setStrIngredient3(String strIngredient3) {
        this.strIngredient3 = strIngredient3;
    }

    public void setStrIngredient4(String strIngredient4) {
        this.strIngredient4 = strIngredient4;
    }

    public void setStrIngredient5(String strIngredient5) {
        this.strIngredient5 = strIngredient5;
    }

    public void setStrIngredient6(String strIngredient6) {
        this.strIngredient6 = strIngredient6;
    }

    public void setStrIngredient7(String strIngredient7) {
        this.strIngredient7 = strIngredient7;
    }

    public void setStrIngredient8(String strIngredient8) {
        this.strIngredient8 = strIngredient8;
    }

    public void setStrIngredient9(String strIngredient9) {
        this.strIngredient9 = strIngredient9;
    }

    public void setStrIngredient10(String strIngredient10) {
        this.strIngredient10 = strIngredient10;
    }

    public void setStrIngredient11(String strIngredient11) {
        this.strIngredient11 = strIngredient11;
    }

    public void setStrIngredient12(String strIngredient12) {
        this.strIngredient12 = strIngredient12;
    }

    public void setStrIngredient13(String strIngredient13) {
        this.strIngredient13 = strIngredient13;
    }

    public void setStrIngredient14(String strIngredient14) {
        this.strIngredient14 = strIngredient14;
    }

    public void setStrIngredient15(String strIngredient15) {
        this.strIngredient15 = strIngredient15;
    }

    public void setStrMeasure1(String strMeasure1) {
        this.strMeasure1 = strMeasure1;
    }

    public void setStrMeasure2(String strMeasure2) {
        this.strMeasure2 = strMeasure2;
    }

    public void setStrMeasure3(String strMeasure3) {
        this.strMeasure3 = strMeasure3;
    }

    public void setStrMeasure4(String strMeasure4) {
        this.strMeasure4 = strMeasure4;
    }

    public void setStrMeasure5(String strMeasure5) {
        this.strMeasure5 = strMeasure5;
    }

    public void setStrMeasure6(String strMeasure6) {
        this.strMeasure6 = strMeasure6;
    }

    public void setStrMeasure7(String strMeasure7) {
        this.strMeasure7 = strMeasure7;
    }

    public void setStrMeasure8(String strMeasure8) {
        this.strMeasure8 = strMeasure8;
    }

    public void setStrMeasure9(String strMeasure9) {
        this.strMeasure9 = strMeasure9;
    }

    public void setStrMeasure10(String strMeasure10) {
        this.strMeasure10 = strMeasure10;
    }

    public void setStrMeasure11(String strMeasure11) {
        this.strMeasure11 = strMeasure11;
    }

    public void setStrMeasure12(String strMeasure12) {
        this.strMeasure12 = strMeasure12;
    }

    public void setStrMeasure13(String strMeasure13) {
        this.strMeasure13 = strMeasure13;
    }

    public void setStrMeasure14(String strMeasure14) {
        this.strMeasure14 = strMeasure14;
    }

    public void setStrMeasure15(String strMeasure15) {
        this.strMeasure15 = strMeasure15;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public int getIdDrink() {
        return idDrink;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrIBA() {
        return strIBA;
    }

    public String getStrAlcoholic() {
        return strAlcoholic;
    }

    public String getStrGlass() {
        return strGlass;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public String getStrIngredient1() {
        return strIngredient1;
    }

    public String getStrIngredient2() {
        return strIngredient2;
    }

    public String getStrIngredient3() {
        return strIngredient3;
    }

    public String getStrIngredient4() {
        return strIngredient4;
    }

    public String getStrIngredient5() {
        return strIngredient5;
    }

    public String getStrIngredient6() {
        return strIngredient6;
    }

    public String getStrIngredient7() {
        return strIngredient7;
    }

    public String getStrIngredient8() {
        return strIngredient8;
    }

    public String getStrIngredient9() {
        return strIngredient9;
    }

    public String getStrIngredient10() {
        return strIngredient10;
    }

    public String getStrIngredient11() {
        return strIngredient11;
    }

    public String getStrIngredient12() {
        return strIngredient12;
    }

    public String getStrIngredient13() {
        return strIngredient13;
    }

    public String getStrIngredient14() {
        return strIngredient14;
    }

    public String getStrIngredient15() {
        return strIngredient15;
    }

    public String getStrMeasure1() {
        return strMeasure1;
    }

    public String getStrMeasure2() {
        return strMeasure2;
    }

    public String getStrMeasure3() {
        return strMeasure3;
    }

    public String getStrMeasure4() {
        return strMeasure4;
    }

    public String getStrMeasure5() {
        return strMeasure5;
    }

    public String getStrMeasure6() {
        return strMeasure6;
    }

    public String getStrMeasure7() {
        return strMeasure7;
    }

    public String getStrMeasure8() {
        return strMeasure8;
    }

    public String getStrMeasure9() {
        return strMeasure9;
    }

    public String getStrMeasure10() {
        return strMeasure10;
    }

    public String getStrMeasure11() {
        return strMeasure11;
    }

    public String getStrMeasure12() {
        return strMeasure12;
    }

    public String getStrMeasure13() {
        return strMeasure13;
    }

    public String getStrMeasure14() {
        return strMeasure14;
    }

    public String getStrMeasure15() {
        return strMeasure15;
    }


    public List<String> getAllIngredients(){

        List<String> ingredients = new ArrayList<String>();

        //if(!this.strIngredient1.isEmpty())
            ingredients.add(this.strIngredient1);
        //if(!this.strIngredient2.isEmpty())
            ingredients.add(this.strIngredient2);
        //if(!this.strIngredient3.isEmpty())
            ingredients.add(this.strIngredient3);
        //if(!this.strIngredient4.isEmpty())
            ingredients.add(this.strIngredient4);
        //if(!this.strIngredient5.isEmpty())
            ingredients.add(this.strIngredient5);
        //if(!this.strIngredient6.isEmpty())
            ingredients.add(this.strIngredient6);
        //if(!this.strIngredient7.isEmpty())
            ingredients.add(this.strIngredient7);
        //if(!this.strIngredient8.isEmpty())
            ingredients.add(this.strIngredient8);
        //if(!this.strIngredient9.isEmpty())
            ingredients.add(this.strIngredient9);
        //if(!this.strIngredient10.isEmpty())
            ingredients.add(this.strIngredient10);
        //if(!this.strIngredient11.isEmpty())
            ingredients.add(this.strIngredient11);
        //if(!this.strIngredient12.isEmpty())
            ingredients.add(this.strIngredient12);
        //if(!this.strIngredient13.isEmpty())
            ingredients.add(this.strIngredient13);
        //if(!this.strIngredient14.isEmpty())
            ingredients.add(this.strIngredient14);
        //if(!this.strIngredient15.isEmpty())
            ingredients.add(this.strIngredient15);

        return ingredients;
    }

    public List<String> getMeasurements(){
        List<String> measuresments = new ArrayList<String>();
        //if(!this.strMeasure1.isEmpty())
            measuresments.add(this.strMeasure1);
        //if(!this.strMeasure2.isEmpty())
            measuresments.add(this.strMeasure2);
        //if(!this.strMeasure3.isEmpty())
            measuresments.add(this.strMeasure3);
        //if(!this.strMeasure4.isEmpty())
            measuresments.add(this.strMeasure4);
        //if(!this.strMeasure5.isEmpty())
            measuresments.add(this.strMeasure5);
        //if(!this.strMeasure6.isEmpty())
            measuresments.add(this.strMeasure6);
        //if(!this.strMeasure7.isEmpty())
            measuresments.add(this.strMeasure7);
        //if(!this.strMeasure8.isEmpty())
            measuresments.add(this.strMeasure8);
        //if(!this.strMeasure9.isEmpty())
            measuresments.add(this.strMeasure9);
        //if(!this.strMeasure10.isEmpty())
            measuresments.add(this.strMeasure10);
        //if(!this.strMeasure11.isEmpty())
            measuresments.add(this.strMeasure11);
        //if(!this.strMeasure12.isEmpty())
            measuresments.add(this.strMeasure12);
        //if(!this.strMeasure13.isEmpty())
            measuresments.add(this.strMeasure13);
        //if(!this.strMeasure14.isEmpty())
            measuresments.add(this.strMeasure14);
        //if(!this.strMeasure15.isEmpty())
            measuresments.add(this.strMeasure15);

        return measuresments;
    }

    public String drinkIngredients(){

        List<String> ingredients = this.getAllIngredients();
        List<String> measurements = this.getMeasurements();

        String ing = "";
        for(int i = 0; i < measurements.size(); i++){

            if(!measurements.get(i).isEmpty() && !ingredients.get(i).isEmpty())
                ing += measurements.get(i) + " " + ingredients.get(i) + "\n";


        }

        return ing;
    }

}
