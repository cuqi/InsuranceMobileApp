package com.example.insurancemobileapp.wsmethods;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.Globals;
import com.example.insurancemobileapp.R;
import com.example.insurancemobileapp.modelclasses.CascoInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Casco extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner type_casco;
    Spinner type_value;
    Spinner franchise;
    EditText vehicle_price;
    CheckBox is_windows;
    Button calculate;
    Button openDialog;


    String input_tc;
    String input_tv;
    int input_f;
    int input_vp;
    boolean input_w;

    String sessionID;

    CascoInfo cascoInfo;

    String code;
    String message;
    String premium;

    Context context;
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
        openDialog = (Button) findViewById(R.id.openDialog);

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

        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        SharedPreferences prefs = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ENUMS SE POPOLNETI OD SPINNER
                input_vp = Integer.parseInt(vehicle_price.getText().toString());
                if (is_windows.isChecked()) {
                    input_w = true;
                } else {
                    input_w = false;
                }
                if (vehicle_price.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Полето 'Цена на возилото' е празно!", Toast.LENGTH_LONG).show();
                }
                sessionID = prefs.getString("sessionid", "12345678");

                GetCascoQuotation getCascoQuotation = new GetCascoQuotation();
                getCascoQuotation.execute();
            }
        });
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

    void showCustomDialog() {
        final Dialog dialog = new Dialog(Casco.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        switch (input_tc) {
            case "BASIC":
                dialog.setContentView(R.layout.basic_casco_dialog);
                break;
            case "PARTIAL":
                dialog.setContentView(R.layout.partial_casco_dialog);
                break;
            case "FULL":
                dialog.setContentView(R.layout.full_casco_dialog);
                break;
            default:
                dialog.dismiss();
        }
        dialog.show();
    }

    public class GetCascoQuotation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(Globals.NAMESPACE, Globals.GET_CASCO);

            cascoInfo = new CascoInfo();
            cascoInfo.setProperty(0, input_tc);
            cascoInfo.setProperty(1, input_w);
            cascoInfo.setProperty(2, input_vp);
            cascoInfo.setProperty(3, input_tv);
            cascoInfo.setProperty(4, input_f);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("CascoInfo");
            pi.setValue(cascoInfo);
            pi.setType(cascoInfo.getClass());

            request.addProperty(pi);
            request.addProperty("sessionID", sessionID);
            Log.i("request data: ", request.getName());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(Globals.NAMESPACE, "CascoInfo", cascoInfo.getClass());

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Globals.URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(Globals.NAMESPACE + Globals.GET_CASCO, envelope);

                Log.i("request dump:", androidHttpTransport.requestDump);
                Log.i("response dump:", androidHttpTransport.responseDump);

                SoapObject result = (SoapObject) envelope.bodyIn;
                if(result != null) {
                    SoapObject getResponse = (SoapObject) result.getProperty(0);

                    code = getResponse.getPrimitivePropertyAsString("code");
                    premium = getResponse.getPrimitivePropertyAsString("premium");
                    message = getResponse.getPrimitivePropertyAsString("message");

                    Log.i("code", code);
                    Log.i("premium", premium);
                } else {
                    Toast.makeText(getApplicationContext(), "No response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return premium;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ONPOSTEX", String.valueOf(result));
            context = getApplicationContext();
            if (code.equals("100")) {
                Intent intent = new Intent(context, BookTravelHealth.class);
                intent.putExtra("cascoInfo", cascoInfo);
                intent.putExtra("sessionid", sessionID);
                intent.putExtra("premiumCasco", premium);
                intent.putExtra("typePolicy", "CAS");
                startActivity(intent);
            } else  {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}