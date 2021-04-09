package com.example.tourismactivationhelperapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Place.class},version = 1)
public abstract class TourismActivationDatabase  extends RoomDatabase {
    public abstract PlaceDAO placeDAO();


    private static TourismActivationDatabase ourInterface;


    public static TourismActivationDatabase getInstance(Context context){

        if(ourInterface == null)
        {
            ourInterface = Room.databaseBuilder(context,
                    TourismActivationDatabase.class,"TourismActivationDatabase.db").
                    createFromAsset("databases/TourismActivationDatabase.db").
                    allowMainThreadQueries().build();

        }
        return  ourInterface;

    }

}
