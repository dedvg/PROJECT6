package com.example.david.project6;

import java.util.List;

/**
 * Created by dedvg on 12-12-2017.
 */

public class UserClass {
    public List<String> Countries;
    public int Score ;
    public String username;

    // set default constructor needed for firebase
    public UserClass(){};

    public UserClass( String username, Integer score, List<String> Countries){
        this.Score = score;
        this.Countries = Countries;
        this.username = username;


    };


}
