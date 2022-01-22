package com.example.insurancemobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.account.LoginActivity;
import com.example.insurancemobileapp.modelclasses.TravelInfo;

public class TestActivity extends AppCompatActivity {

    EditText myNum;
    String numValue;
    Button calculate;
    String calcResult;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TravelInfo ti = new TravelInfo();
        ti.setProperty(0, "yes");

        TestClass tc = new TestClass();
        tc.setX("blabla");
        tc.setY(1);
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("myParcel", tc);
        i.putExtra("myParcel2", ti);
        startActivity(i);
    }
}