package com.academy.learnprogramming.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserDetails {
    private String number;
    private String userName;
    private Boolean working;
    private Boolean workingwithDep;
    private Boolean workingwithHugeDep;
    public Map<String, Boolean> stars = new HashMap<>();

    public UserDetails() {
    }

    public UserDetails(String number, String userName) {
        this.number = number;
        this.userName = userName;
    }

    public UserDetails(String number, String userName, Boolean working, Boolean workingwithDep, Boolean workingwithHugeDep) {
        this.number = number;
        this.userName = userName;
        this.working = working;
        this.workingwithDep = workingwithDep;
        this.workingwithHugeDep = workingwithHugeDep;
    }

    public void setNumber(String number) {
        this.number = number;
    }
//
    public String getNumber() {
        return number;
    }
//
    public String getUserName() { return userName; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //
//    public String getUserName() {
//        return userName;
//    }

    @Exclude
    public Map<String, Object> toMap(String cfr, Class<String> stringClass) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("number", number);
        result.put("userName", userName);
        result.put("working", working);
        result.put("workingwithDep", workingwithDep);
        result.put("workingwithHugeDep", workingwithHugeDep);
        result.put("stars", stars);

        return result;
    }


}
