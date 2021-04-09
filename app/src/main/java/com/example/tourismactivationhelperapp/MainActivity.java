package com.example.tourismactivationhelperapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class MainActivity extends AppCompatActivity implements TagView.OnTagClickListener {
    SharedPreferences perf;
    private static final String TAG = "insert into backendless";
    int USER_ID ;
    List<String> tags = Arrays.asList("Local", "Calm", "Historical","Free","Family");
    List<String> categories = Arrays.asList("Hotel", "Restaurant", "Historical","Local");
    List<String> governorates = Arrays.asList("Cairo", "Giza", "Alexandria", "Dakahlia", "Alsharqia", "Monufia", "Qalyubia", "Elbeheira", "Algharbia", "Port Said", "Damietta", "Ismailia", "Suez", "Kafr Elsheikh", "Fayoum", "Bani Sweif", "Jebal Resort", "Matrouh", "El Alamein", "Siwa", "North Sinai", "South Sinai", "Tor Sinai", "Minya", "Asyut", "Sohag", "Qena", "The Red Sea", "Luxor", "Aswan", "Oasis", "The New Valley", "Toshka");

    HashMap<String,Object> placeFullMap= new HashMap<>();
    HashMap<String,Integer> reviews= new HashMap<>();
    HashMap<String, Object> placeInformationMap = new HashMap<>();
    ArrayList<String> imageArrayList= new ArrayList<>();
    ArrayList<Price> priceArrayList= new ArrayList<>();
    ArrayAdapter<String> adapter;
    PriceAdapter priceAdapter;
    ImageAdapter imageAdapter;

    PlaceDAO placeDAO;
    //views
    Button insertButton;
    TagContainerLayout tagContainerLayout;
    Spinner categorySpinner ;
    SearchableSpinner governoratesSpinner ;
    ListView priceListView,imageListView;
    EditText imageEditText,priceNameEditText,costEditText,nameEditText,locationEditText,descriptionEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        perf = getSharedPreferences("data",Context.MODE_PRIVATE);
        Backendless.initApp(this,"936A3E76-2AE2-B9CF-FF53-BADBF82A7500","31DAB2C3-A6AA-4476-8A08-1DB2F1444759");


        //category Spinner setting
        categorySpinner = findViewById(R.id.categorySpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,categories);
        categorySpinner.setAdapter(adapter);

        // governorates Spinner settings
        governoratesSpinner = findViewById(R.id.governorateSpinner);
        governoratesSpinner.setTitle("Select Item");
        governoratesSpinner.setPositiveButton("OK");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,governorates);
        governoratesSpinner.setAdapter(adapter);


        // tag Container settings
        tagContainerLayout = findViewById(R.id.tagcontainerLayout);
        tagContainerLayout.setIsTagViewSelectable(true);
        tagContainerLayout.setOnTagClickListener(this);
        tagContainerLayout.setTags(tags);


        //price array list test
        priceListView = findViewById(R.id.priceListView);
        priceAdapter = new PriceAdapter(this,priceArrayList);
        priceListView.setAdapter(priceAdapter);


        // image list view
        imageListView = findViewById(R.id.imageListView);
        imageAdapter = new ImageAdapter(this,imageArrayList);
        imageListView.setAdapter(imageAdapter);

        placeDAO = TourismActivationDatabase.getInstance(this).placeDAO();

        // views
        imageEditText = findViewById(R.id.imageEditText);
        costEditText = findViewById(R.id.costEditText);
        priceNameEditText = findViewById(R.id.priceNameEditText);
        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        insertButton = findViewById(R.id.insertButton);

        USER_ID =  perf.getInt("user",-1);
        if(USER_ID == -1)
        {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        USER_ID = perf.getInt("user",-1);
    }

    public void addImage(View view) {
        String image = imageEditText.getText().toString();
        if(image.isEmpty())
        {
            Toast.makeText(this, "Enter image link ,please", Toast.LENGTH_SHORT).show();
            return;
        }
        //adding image link into list view
        imageArrayList.add(imageEditText.getText().toString());
        imageAdapter.notifyDataSetChanged();
        imageEditText.setText("");//clean imageEditText
        image = null;
    }


    public void addPrice(View view) {

        String name = priceNameEditText.getText().toString();

        if(name.isEmpty() || costEditText.getText().toString().isEmpty() )
        {
            Toast.makeText(this, "Enter name and price ,please", Toast.LENGTH_SHORT).show();
            return;
        }
        int cost = Integer.parseInt(costEditText.getText().toString());
        priceArrayList.add(new Price(name,cost));
        priceAdapter.notifyDataSetChanged();

        // clean EditText
        priceNameEditText.setText("");
        costEditText.setText("");

        name = null;

    }

    void setPlaceMap() {
        placeInformationMap.put( "name", nameEditText.getText().toString() );
        placeInformationMap.put( "location",locationEditText.getText().toString());
        placeInformationMap.put( "description", descriptionEditText.getText().toString() );
        placeInformationMap.put( "tags", tagContainerLayout.getSelectedTagViewText() );
        placeInformationMap.put( "images", imageArrayList);
        placeInformationMap.put( "prices", priceArrayList);

        // full map
        placeFullMap.put("place_information",placeInformationMap);//place column
        placeFullMap.put("category",categorySpinner.getSelectedItem().toString());//category column
        placeFullMap.put("governorate",governoratesSpinner.getSelectedItem().toString());//governorate column
        placeFullMap.put("user_id",USER_ID);//user_id column
        placeFullMap.put("reviews",reviews);//reviews


    }

    boolean isFull() {
        if(nameEditText.getText().toString().isEmpty())
            return false;

        if(locationEditText.getText().toString().isEmpty())
            return false;

        if(tagContainerLayout.getSelectedTagViewText().size() == 0)
            return false;

        if(priceArrayList.size() == 0 || imageArrayList.size() == 0)
            return false;

        return true;
    }

    Gson gson = new Gson();

    public void insert(View view) {

        if (!isFull())
        {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
            return;
        }

        insertButton.setEnabled(false);
        setPlaceMap();
        //insert in to Backendless request
        Backendless.Data.of("places").save(placeFullMap, new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map response) {


                Toast.makeText(MainActivity.this, "Place has been inserted successfully", Toast.LENGTH_SHORT).show();
                //insert in to room
                Place p = new Place(USER_ID
                        ,governoratesSpinner.getSelectedItem().toString()
                        ,categorySpinner.getSelectedItem().toString()
                        ,gson.toJsonTree(placeInformationMap).getAsJsonObject().toString());
                placeDAO.insertPlace(p);
                p.category = null;
                p.governorate = null;
                p.place_information = null;
                p = null;

                Log.i(TAG, response.toString());
                rest();
                insertButton.setEnabled(true);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MainActivity.this, "there is a problem into insert,\ntry again,please", Toast.LENGTH_SHORT).show();
                Log.e(TAG, fault.getMessage());
                insertButton.setEnabled(true);
            }
        });



    }

    void rest() {
        //views cleaning
        nameEditText.setText("");
        costEditText.setText("");
        imageEditText.setText("");
        locationEditText.setText("");
        priceNameEditText.setText("");
        descriptionEditText.setText("");
        for (int i = 0; i < tags.size(); i++) {
            tagContainerLayout.deselectTagView(i);
        }

        // data
         placeFullMap.clear();
         placeInformationMap.clear();
         imageArrayList.clear();
         priceArrayList.clear();
         priceAdapter.notifyDataSetChanged();
         imageAdapter.notifyDataSetChanged();


    }


    public void exportDatabase(MenuItem item) {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);

        // nullify
        intent = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    class PriceAdapter extends ArrayAdapter<Price>{

        public PriceAdapter(@NonNull Context context, List<Price> prices ) {
            super(context, 0,prices);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            PriceViewHolder holder;

            // we made this if .. to reuse convertView and viewHolder
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.price_layout,parent,false);
                holder = new PriceViewHolder(convertView);
                convertView.setTag(holder);
            }else
            {
                holder = (PriceViewHolder) convertView.getTag();
            }

            // set data into the views
            holder.NameTextView.setText(getItem(position).name);
            holder.costTextView.setText(getItem(position).cost + " LE");

            // lucky code xD
            holder.removeImageView.setOnClickListener(v -> {
                priceArrayList.remove(position);
                priceAdapter.notifyDataSetChanged();
            });
            return  convertView;
        }

    }


    //we made this class to reuse the TextView id's because findViewById is expensive operation
    class PriceViewHolder {
        TextView costTextView,NameTextView;
        ImageView removeImageView;
        public  PriceViewHolder (View convertView)
        {
            removeImageView = convertView.findViewById(R.id.removeImageView);
            costTextView = convertView.findViewById(R.id.costTextView);
            NameTextView = convertView.findViewById(R.id.imageTextView);

        }
    }





    class ImageAdapter extends ArrayAdapter<String>{

        public ImageAdapter(@NonNull Context context, List<String> images ) {
            super(context, 0,images);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ImageViewHolder holder;

            // we made this if .. to reuse convertView and viewHolder
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.img_layout,parent,false);
                holder = new ImageViewHolder(convertView);
                convertView.setTag(holder);
            }else
            {
                holder = (ImageViewHolder) convertView.getTag();
            }

            // set data into the views
            holder.imageTextView.setText(getItem(position));


            // lucky code xD
            holder.imageView.setOnClickListener(v -> {
                imageArrayList.remove(position);
                imageAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Image removed", Toast.LENGTH_SHORT).show();
            });
            return  convertView;
        }

    }


    //we made this class to reuse the TextView id's because findViewById is expensive operation
    class ImageViewHolder {
        TextView imageTextView;
        ImageView imageView;
        public  ImageViewHolder (View convertView)
        {
            imageView = convertView.findViewById(R.id.imageView);
            imageTextView = convertView.findViewById(R.id.imageTextView);


        }
    }







    boolean isSelected(byte position) {

        for (Integer tagPosition : tagContainerLayout.getSelectedTagViewPositions()) {
            if(position == tagPosition)
                return true;
        }
        return false;
    }

    @Override
    public void onTagClick(int position, String text) {

        if(isSelected((byte) position))
            tagContainerLayout.deselectTagView(position);
        else
            tagContainerLayout.selectTagView(position);
        
    }

    @Override
    public void onTagLongClick(int position, String text) {

    }

    @Override
    public void onSelectedTagDrag(int position, String text) {

    }

    @Override
    public void onTagCrossClick(int position) {

    }
}