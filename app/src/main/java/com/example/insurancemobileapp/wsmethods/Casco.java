package com.example.insurancemobileapp.wsmethods;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.R;


public class Casco extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner type_casco;
    Spinner type_value;
    Spinner franchise;
    EditText vehicle_price;
    CheckBox is_windows;
    Button calculate;

    String input_tc;
    String input_tv;
    int input_f;
    String input_vp;
    boolean input_w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casco);

        type_casco = (Spinner) findViewById(R.id.type_casco);
        type_value = (Spinner) findViewById(R.id.type_value);
        franchise = (Spinner) findViewById(R.id.franchise);
        vehicle_price = (EditText) findViewById(R.id.vehicle_price);
        is_windows = (CheckBox) findViewById(R.id.isWindows);
        calculate = (Button) findViewById(R.id.calcCasco);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Casco.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type_casco));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_casco.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Casco.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type_value));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_value.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Casco.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.franchise));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        franchise.setAdapter(adapter3);

        type_casco.setOnItemSelectedListener(this);
        type_value.setOnItemSelectedListener(this);
        franchise.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.type_casco:
                if (parent.getItemAtPosition(position).toString().equals("Основно")) {
                    input_tc = "BASIC";
                } else if (parent.getItemAtPosition(position).toString().equals("Парцијално")) {
                    input_tc = "PARTIAL";
                } else if (parent.getItemAtPosition(position).toString().equals("Целосно")) {
                    input_tc = "FULL";
                }
                break;
            case R.id.type_value:
                if (parent.getItemAtPosition(position).toString().equals("Новонабавна")) {
                    input_tv = "NEW";
                } else if (parent.getItemAtPosition(position).toString().equals("Пазарна")) {
                    input_tv = "MARKET";
                }
                break;
            case R.id.franchise:
                input_f = Integer.valueOf(parent.getItemAtPosition(position).toString());
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}