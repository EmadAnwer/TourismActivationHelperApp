package com.example.tourismactivationhelperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {
    EditText editText;
    SharedPreferences perf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        perf = getSharedPreferences("data", Context.MODE_PRIVATE);
        editText = findViewById(R.id.editTextNumber);
    }

    public void setUserID(View view) {
        SharedPreferences.Editor editor = perf.edit();
        editor.putInt("user", Integer.parseInt(editText.getText().toString()));
        editor.apply();
        super.onBackPressed();
    }
}