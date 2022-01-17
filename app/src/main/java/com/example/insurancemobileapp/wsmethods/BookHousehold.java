package com.example.insurancemobileapp.wsmethods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.R;

public class BookHousehold extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_household);

        Intent intent = getIntent();
        String input_to = intent.getStringExtra("input_to");
        String input_tc = intent.getStringExtra("input_tc");
        int input_ao = intent.getIntExtra("input_ao", 30);
        String input_do = intent.getStringExtra("input_do");
        int input_cl = intent.getIntExtra("input_cl", 1);

        Log.i("input_tp", input_to);
    }
}