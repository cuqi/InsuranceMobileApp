package com.example.insurancemobileapp.wsmethods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.Globals;
import com.example.insurancemobileapp.R;
import com.example.insurancemobileapp.modelclasses.HouseholdInfo;
import com.example.insurancemobileapp.modelclasses.TravelInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Household extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // HTML ELEMENTS
    Spinner typeObject;
    Spinner typeCover;
    EditText areaOfObject;
    EditText dateOfObject;
    Spinner contractLength;
    Button calculate;

    // DATA ELEMENTS
    String input_to;
    String input_tc;
    int input_ao;
    String input_do;
    int input_cl;

    String sessionID;
    String res;

    String code;
    String premium;
    String message;

    Context context;
    TravelInfo travelInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household);

        typeObject = (Spinner) findViewById(R.id.household_type_object);
        typeCover = (Spinner) findViewById(R.id.household_type_cover);
        areaOfObject = (EditText) findViewById(R.id.areaOfObject);
        dateOfObject = (EditText) findViewById(R.id.dateOfObject);
        contractLength = (Spinner) findViewById(R.id.contractLength);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Household.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.householdTypeObject));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeObject.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Household.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.householdTypeCover));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCover.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Household.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.householdContractLength));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contractLength.setAdapter(adapter3);

        typeObject.setOnItemSelectedListener(this);
        typeCover.setOnItemSelectedListener(this);
        contractLength.setOnItemSelectedListener(this);

        calculate = (Button) findViewById(R.id.calcHousehold);
        SharedPreferences prefs = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ENUMS SE POPOLNETI OD SPINNER
                input_ao = Integer.parseInt(areaOfObject.getText().toString());
                input_do = dateOfObject.getText().toString();

                sessionID = prefs.getString("sessionid", "12345678");
                GetHouseholdQuotation getHouseholdQuotation = new GetHouseholdQuotation();
                getHouseholdQuotation.execute();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.household_type_object:
                input_to = parent.getItemAtPosition(position).toString().toUpperCase();
                break;
            case R.id.household_type_cover:
                input_tc = parent.getItemAtPosition(position).toString();
                break;
            case R.id.contractLength:
                input_cl = Integer.valueOf(parent.getItemAtPosition(position).toString());
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public class GetHouseholdQuotation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // SESSION ID : $2a$10$t/OiwGwK7gzQ5acbGoJmRe
        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(Globals.NAMESPACE, Globals.GET_HOUSEHOLD);

            HouseholdInfo householdInfo = new HouseholdInfo();
            householdInfo.setProperty(0, input_to);
            householdInfo.setProperty(1, input_tc);
            householdInfo.setProperty(2, input_ao);
            householdInfo.setProperty(3, input_do);
            householdInfo.setProperty(4, input_cl);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("HouseholdInfo");
            pi.setValue(householdInfo);
            pi.setType(householdInfo.getClass());

            request.addProperty(pi);
            request.addProperty("sessionID", sessionID);
            Log.i("request data: ", request.getName());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(Globals.NAMESPACE, "HouseholdInfo", householdInfo.getClass());

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Globals.URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(Globals.NAMESPACE + Globals.GET_HOUSEHOLD, envelope);

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
                Intent intent = new Intent(context, BookHousehold.class);
                intent.putExtra("input_to", input_to);
                intent.putExtra("input_tc", input_tc);
                intent.putExtra("input_ao", input_ao);
                intent.putExtra("input_do", input_do);
                intent.putExtra("input_cl", input_cl);
                startActivity(intent);
            } else  {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}