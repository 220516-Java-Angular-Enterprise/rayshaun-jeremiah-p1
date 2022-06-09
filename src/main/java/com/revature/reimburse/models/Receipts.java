package com.revature.reimburse.models;

public class Receipts {
    private String location;

    public Receipts(){
        super();
    }

    public Receipts(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
