package com.example.tourismactivationhelperapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prices")
public class Price {

    public String name;
    public int cost;

    public Price() {

    }
    public Price(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }


}
