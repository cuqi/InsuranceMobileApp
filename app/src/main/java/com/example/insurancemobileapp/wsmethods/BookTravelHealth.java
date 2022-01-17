package com.example.insurancemobileapp.wsmethods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.R;

public class BookTravelHealth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_travel_health);

        Intent intent = getIntent();
        String input_tp = intent.getStringExtra("input_tp");
        String input_tc = intent.getStringExtra("input_tc");
        int input_nd = intent.getIntExtra("input_nd", 3);
        String input_country = intent.getStringExtra("input_country");
        int input_np = intent.getIntExtra("input_np", 1);

        Log.i("input_tp", input_tp);
    }
}