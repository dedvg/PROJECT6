package com.example.david.project6;

/**
 * Created by dedvg on 7-12-2017.
 */

public class CountryClass {

    public String country;
    public String capital ;
    // set default constructor needed for firebase
    public CountryClass(){};

    public CountryClass(String country, String capital){
        this.country = country;
        this.capital = capital;
    };

}
