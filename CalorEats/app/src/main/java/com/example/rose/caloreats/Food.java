package com.example.rose.caloreats;

public class Food {

    private String name;
    private String cals;
    private String price;
    private double dist;
    private String address;
    private String UID;
    private String restaurant;

    public Food(String name_, String cals_, String price_, String address_){
        name = name_;
        cals = cals_;
        price = price_;
        address = address_;
        dist = calculateDist();
    }

    public Food(String name_, String cals_, String price_){
        name = name_;
        cals = cals_;
        price = price_;
    }

    public void setUserID(String id) { UID = id; }

    public String getName(){
        return name;
    }

    public String getCals(){
        return cals;
    }

    public String getPrice(){
        return price;
    }

    public String getRestaurant(){ return restaurant; }

    public String getAddress() { return address; }

    public double getDist(){ return dist; }

    public String getUID(){ return UID; }

    private double calculateDist(){
        return 0;
    }

    public String print(){
        return ("Name: " + name + ", Cals: " + cals + ", Price: " + price + ", UID: " + UID);
    }

    public void setRestaurant(String restaurant){
        this.restaurant = restaurant;
    }

    public String getFoodId(){
        String foodID = (restaurant+"/"+name).replaceAll(" ", "_").toLowerCase();
        return foodID;
    }



}
