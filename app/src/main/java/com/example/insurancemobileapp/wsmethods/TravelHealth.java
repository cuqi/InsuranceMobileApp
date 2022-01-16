package com.example.insurancemobileapp.wsmethods;

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
import com.example.insurancemobileapp.modelclasses.TravelInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class TravelHealth extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // HTML ELEMENTS
    Spinner typePolicy;
    Spinner typeCover;
    EditText numDays;
    EditText country;
    EditText numPeople;
    Button calculate;

    // DATA ELEMENTS
    String input_tp;
    String input_tc;
    int input_nd;
    int input_np;
    String input_country;

    String res;
    int premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_health);

        typePolicy = (Spinner) findViewById(R.id.travel_type_policy);
        typeCover = (Spinner) findViewById(R.id.travel_type_cover);
        numDays = (EditText) findViewById(R.id.numDays);
        country = (EditText) findViewById(R.id.country);
        numPeople = (EditText) findViewById(R.id.numPeople);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(TravelHealth.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.travelTypePolicy));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typePolicy.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TravelHealth.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.travelTypeCover));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCover.setAdapter(adapter2);


        typePolicy.setOnItemSelectedListener(this);
        typeCover.setOnItemSelectedListener(this);

        calculate = (Button) findViewById(R.id.calcTravel);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ENUMS SE POPOLNETI OD SPINNER
                input_nd = Integer.parseInt(numDays.getText().toString());
                input_np = Integer.parseInt(numPeople.getText().toString());
                input_country = country.getText().toString();

                GetTravelQuotation getTravelQuotation = new GetTravelQuotation();
                getTravelQuotation.execute();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.travel_type_policy:
                input_tp = parent.getItemAtPosition(position).toString().toUpperCase();
                break;
            case R.id.travel_type_cover:
                input_tc = parent.getItemAtPosition(position).toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class GetTravelQuotation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // SESSION ID : $2a$10$t/OiwGwK7gzQ5acbGoJmRe
        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(Globals.NAMESPACE, Globals.GET_TRAVEL);
            TravelInfo travelInfo = new TravelInfo();
            travelInfo.setProperty(0, input_tp);
            travelInfo.setProperty(1, input_tc);
            travelInfo.setProperty(2, input_nd);
            travelInfo.setProperty(3, input_country);
            travelInfo.setProperty(4, input_np);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("travelInfo");
            pi.setValue(travelInfo);
            pi.setType(travelInfo.getClass());

            request.addProperty(pi);
            request.addProperty("sessionID", "$2a$10$t/OiwGwK7gzQ5acbGoJmRe");
            Log.i("request data: ", request.getName());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(Globals.NAMESPACE, "travelInfo", travelInfo.getClass());

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Globals.URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(Globals.NAMESPACE + Globals.GET_TRAVEL, envelope);

                Log.i("request dump:", androidHttpTransport.requestDump);
                Log.i("response dump:", androidHttpTransport.responseDump);

                SoapObject result = (SoapObject) envelope.bodyIn;
                if(result != null) {
                    res = result.getProperty(0).toString();
                    Log.i("RESULT CALC", res);
                } else {
                    Toast.makeText(getApplicationContext(), "No response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ONPOSTEX", String.valueOf(result));

        }
    }
}