package com.example.b07project;

public class Event {


    private String name;
    //For Customers created by reading from database
    public boolean addName(String name){
        if(this.name == null){
            this.name = name;
            return true;
        }
        return false;
    }
}
