package com.example.david.project6;

import java.util.List;

/**
 * Created by dedvg on 14-12-2017.
 */

public class UserList {
    public List<UserClass> userlist;


    // set default constructor needed for firebase
    public UserList(){};

    public UserList(List<UserClass> userlist){
        this.userlist = userlist;



    }


}
