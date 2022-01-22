package com.example.insurancemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity {

    TextView tv_policyID;
    String policyID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        policyID = intent.getStringExtra("policyID");
        tv_policyID = (TextView) findViewById(R.id.policyID);
        tv_policyID.setText("Policy number:" + policyID);
    }
}