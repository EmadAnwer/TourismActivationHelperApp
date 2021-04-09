package com.example.tourismactivationhelperapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "places")
public class Place {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int user_id;
    public String governorate,category,place_information;


    public Place(int user_id, String governorate, String category, String place_information) {
        this.user_id = user_id;
        this.governorate = governorate;
        this.category = category;
        this.place_information = place_information;
    }
}
