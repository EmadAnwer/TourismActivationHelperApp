package com.example.tourismactivationhelperapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    short counter = 0;
    short from;
    short count;
    List<Place> offset10Places;
    Button previousButton, nextButton;
    PlaceDAO placeDAO;
    TextView countTV,counterTV;
    StringBuilder jsonPlace = new StringBuilder();
    StringBuilder jsonPlaceInfo = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        placeDAO = TourismActivationDatabase.getInstance(this).placeDAO();
        countTV = findViewById(R.id.countTV);
        counterTV = findViewById(R.id.counterTV);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        count = placeDAO.placesCount();
        countTV.append(Short.toString(count));

    }
     ClipboardManager myClipboard;
     ClipData myClip;
    public void delete(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(DataActivity.this).create();
        alertDialog.setTitle("WARNING");
        alertDialog.setMessage("is you press Ok all data in will disappear,\n you can stop this by clicking Cancel ");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        placeDAO.deleteAll();
                        dialog.dismiss();
                        onBackPressed();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }
    public void next(View view) {

        previousButton.setEnabled(true);

        if(counter == 0)
            previousButton.setEnabled(false);

        counterTV.setText("");
        counterTV.append("copied from ");
        from = counter;
        counterTV.append(Short.toString(counter));
        counterTV.append("  to  ");
        Select();
        counter +=10;


        if(counter > count)
        {
            counterTV.append(Short.toString(count));
            counter = count;
            nextButton.setEnabled(false);
        }
        else if(counter == count)
        {
            nextButton.setEnabled(false);
            counterTV.append(Short.toString((short) (counter)));

        }
        else
            counterTV.append(Short.toString((short) (counter)));




    }


    public void previous(View view) {


        nextButton.setEnabled(true);

        counterTV.setText("");
        counterTV.append("copied from ");
        counterTV.append(Short.toString((short) (from -10)));
        from = (short) (from -10);
        counterTV.append("  to  ");

        counter = from;
        Select();


        if(counter == 0)
        {
            previousButton.setEnabled(false);
        }
        counter+=10;
        counterTV.append(Short.toString((short) (counter)));



    }


    void  Select() {
        jsonPlace.append("[\n");
        offset10Places =  placeDAO.Offset10Places((short) (counter));
        if(offset10Places.size() == 0)
        {
            return;
        }

        for (Place place : offset10Places) {
            jsonPlaceInfo.append(place.place_information);
            jsonPlace.append("{\n" + "  \"governorate\":\"").append(place.governorate).append("\",\n  \"category\":\"").append(place.category).append("\", \n").append(" \"place_information\" : ").append(jsonPlaceInfo).append("},\n");
            jsonPlaceInfo.delete(0,jsonPlaceInfo.length());
        }

        jsonPlace.setCharAt(jsonPlace.length()-2,' ');
        jsonPlace.append("]");
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", jsonPlace.toString());
        Toast.makeText(this, "Places has been copied", Toast.LENGTH_SHORT).show();
        myClipboard.setPrimaryClip(myClip);
        jsonPlace.delete(0,jsonPlace.length());
        jsonPlaceInfo.delete(0,jsonPlaceInfo.length());
        offset10Places.clear();
    }

    public void copyAgain(View view) {
        if(myClipboard != null)
        {
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(this, "Places has been copied Again", Toast.LENGTH_SHORT).show();
        }
    }


    //old
    /*
    void  Select() {
        jsonPlace.append("[\n");
        offset10Places =  placeDAO.Offset10Places(counter);
        if(offset10Places.size() == 0)
            max = true;



        for (Place place : offset10Places) {
            jsonPlaceInfo.append(place.place_information);
            jsonPlaceInfo.delete(0,80);
            jsonPlaceInfo.delete(jsonPlaceInfo.length()-200,jsonPlaceInfo.length());
            jsonPlace.append("{\n" + "  \"governorate\":\"").append(place.governorate).append("\",\n  \"category\":\"").append(place.category).append("\", \n").append(" \"place_information\" : ").append(jsonPlaceInfo).append("\"}},\n");
            jsonPlaceInfo.delete(0,jsonPlaceInfo.length());
        }

        jsonPlace.setCharAt(jsonPlace.length()-2,' ');
        jsonPlace.append("]");

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", jsonPlace.toString());
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        jsonPlace.delete(0,jsonPlace.length());
        jsonPlaceInfo.delete(0,jsonPlaceInfo.length());
        offset10Places.clear();
    }

     */
    /*
    public void extract(View view) {
        editTextTextPersonName.setText("");
        for (Place place : placeDAO.allPlaces()) {
            jsonPlace.append("{\"user_id\": \"")
                    .append(place.user_id)
                    .append("\",\"category\":\"")
                    .append(place.category)
                    .append("\",\"governorate\":\"")
                    .append(place.governorate)
                    .append("\",\"place_information\":\"")
                    .append(place.place_information)
                    .append("\"\n},\n\n");

        }
        editTextTextPersonName.setText(jsonPlace);
        editTextTextPersonName.setEnabled(true);

        Button b= (Button) view;
        b.setEnabled(false);
    }
     */
    /*

    public void next(View view) {
        if(!fTime)
            counter +=10;

        Select();
        if(max)
        {
            Toast.makeText(this, "there is no next places", Toast.LENGTH_SHORT).show();
            nextButton.setEnabled(false);
            return;
        }

        counterTV.setText("");
        counterTV.append("you are copied from ");
        counterTV.append(Short.toString(counter));
        counterTV.append("  to  ");

        if(counter > count)
            counterTV.append(Short.toString(count));

        else
            counterTV.append(Short.toString((short) (counter+10)));

        previousButton.setEnabled(true);
        fTime = false;


    }


    public void previous(View view) {


        if(counter == -10)
        {

            Toast.makeText(this, "there is no previous places", Toast.LENGTH_SHORT).show();
            return;
        }


        max = false;
        nextButton.setEnabled(true);

        counterTV.setText("");
        counterTV.append("you are copied from ");
        if(counter > count)
            counterTV.append(Short.toString(count));
        else
            counterTV.append(Short.toString(counter));

        counterTV.append("  to  ");
        counter -=10 ;

        if(counter < 0)
        {
            counterTV.append(Short.toString((short) 0));

        }
        else
            counterTV.append(Short.toString((short) (counter)));

        if(counter < 0 )
        {
            previousButton.setEnabled(false);
        }

        Select();




    }


    void  Select() {


        jsonPlace.append("[\n");
        offset10Places =  placeDAO.Offset10Places(counter);
        if(offset10Places.size() == 0)
        {
            nextButton.setEnabled(false);
            max = true;
            return;

        }



        for (Place place : offset10Places) {
            jsonPlaceInfo.append(place.place_information);
            jsonPlace.append("{\n" + "  \"governorate\":\"").append(place.governorate).append("\",\n  \"category\":\"").append(place.category).append("\", \n").append(" \"place_information\" : ").append(jsonPlaceInfo).append("},\n");
            jsonPlaceInfo.delete(0,jsonPlaceInfo.length());
        }

        jsonPlace.setCharAt(jsonPlace.length()-2,' ');
        jsonPlace.append("]");

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", jsonPlace.toString());
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        jsonPlace.delete(0,jsonPlace.length());
        jsonPlaceInfo.delete(0,jsonPlaceInfo.length());
        offset10Places.clear();
    }

 */
}