package com.example.tourismactivationhelperapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlaceDAO {

    @Insert
    void insertPlace(Place place);

    @Query("SELECT * FROM places")
    List<Place> allPlaces();


    @Query("SELECT COUNT(id) from places")
    short placesCount();

    @Query("SELECT * FROM places LIMIT 10 OFFSET :count")
    List<Place> Offset10Places(short count);


    @Query("DELETE FROM places")
    void deleteAll();

}
