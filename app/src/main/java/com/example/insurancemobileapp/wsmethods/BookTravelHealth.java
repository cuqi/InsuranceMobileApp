package com.example.insurancemobileapp.wsmethods;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.ConfirmPolicy;
import com.example.insurancemobileapp.Globals;
import com.example.insurancemobileapp.R;
import com.example.insurancemobileapp.modelclasses.AOInfo;
import com.example.insurancemobileapp.modelclasses.AccidentInfo;
import com.example.insurancemobileapp.modelclasses.CascoInfo;
import com.example.insurancemobileapp.modelclasses.HouseholdInfo;
import com.example.insurancemobileapp.modelclasses.InsuredInfo;
import com.example.insurancemobileapp.modelclasses.TravelInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookTravelHealth extends AppCompatActivity {

    // podatoci za premija
    String input_tp;
    String input_tc;
    int input_nd;
    String input_country;
    int input_np;

    // podatoci za dogovoruvach
    EditText contr_fn;
    EditText contr_ln;
    EditText contr_ssn;
    EditText contr_addr;
    EditText contr_pc;
    EditText contr_city;

    // podatoci za osigurenik
    EditText ins_fn;
    EditText ins_ln;
    EditText ins_ssn;
    EditText ins_addr;
    EditText ins_pc;
    EditText ins_city;

    // widgets
    TextView text_premium;
    CheckBox isInsured;
    Button createPolicy;
    ScrollView sv;

    // getData podatoci
    String input_c_fn;
    String input_c_ln;
    String input_c_ssn;
    String input_c_addr;


    final Calendar myCalendar = Calendar.getInstance();
    EditText start_date;
    String startDate;
    String sessionID;
    InsuredInfo contractor;
    InsuredInfo insured;
    String typePolicy;
    String typePolicyMapped;

    // premium data info
    TravelInfo travelInfo;
    HouseholdInfo householdInfo;
    AOInfo aoInfo;
    CascoInfo cascoInfo;
    AccidentInfo accidentInfo;

    // return date
    String message;
    String code;
    String policyID;
    String premium;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_travel_health);

        Intent intent = getIntent();
        typePolicy = intent.getStringExtra("typePolicy");
        switch (typePolicy){
            case "TRA":
                travelInfo = (TravelInfo) intent.getSerializableExtra("travelInfo");
                premium = intent.getStringExtra("premiumTravel");
                typePolicyMapped = "travelPolicy";
                Log.i("INSIDE SWITHC TRA", premium);
               break;
            case "HHL":
                householdInfo = (HouseholdInfo) intent.getSerializableExtra("householdInfo");
                premium = intent.getStringExtra("premiumHousehold");
                typePolicyMapped = "householdPolicy";
                Log.i("INSIDE SWITHC HHL", premium);
                break;
            case "AO":
                aoInfo = (AOInfo) intent.getSerializableExtra("aoInfo");
                premium = intent.getStringExtra("premiumAO");
                typePolicyMapped = "aoPolicy";
                Log.i("INSIDE SWITHC AO", premium);
                break;
            case "CAS":
                cascoInfo = (CascoInfo) intent.getSerializableExtra("cascoInfo");
                premium = intent.getStringExtra("premiumCasco");
                typePolicyMapped = "cascoPolicy";
                Log.i("INSIDE SWITHC CAS", premium);
                break;
            case "ACC":
                accidentInfo = (AccidentInfo) intent.getSerializableExtra("accidentInfo");
                premium = intent.getStringExtra("premiumAccident");
                typePolicyMapped = "accidentPolicy";
                Log.i("INSIDE SWITHC CAS", premium);
                break;
            default:
                break;
        }
        sessionID = intent.getStringExtra("sessionid");

        text_premium = (TextView) findViewById(R.id.textPremium);
        text_premium.setText("Премијата изнесува: " + premium + " денари!");
        contr_fn = (EditText) findViewById(R.id.contractor_first_name);
        contr_ln = (EditText) findViewById(R.id.contractor_last_name);
        contr_ssn = (EditText) findViewById(R.id.contractor_ssn);
        contr_addr = (EditText) findViewById(R.id.contractor_address);
        contr_pc = (EditText) findViewById(R.id.contractor_postal);
        contr_city = (EditText) findViewById(R.id.contractor_city);

        ins_fn = (EditText) findViewById(R.id.insured_first_name);
        ins_ln = (EditText) findViewById(R.id.insured_last_name);
        ins_ssn = (EditText) findViewById(R.id.insured_ssn);
        ins_addr = (EditText) findViewById(R.id.insured_address);
        ins_pc = (EditText) findViewById(R.id.insured_postal);
        ins_city = (EditText) findViewById(R.id.insured_city);

        // widgets
        sv = (ScrollView) findViewById(R.id.scrollView2);
        sv.setVisibility(View.GONE);
        isInsured = (CheckBox) findViewById(R.id.checkOwner);
        createPolicy = (Button) findViewById(R.id.createPolicy);
        start_date = (EditText) findViewById(R.id.start_date);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BookTravelHealth.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        isInsured.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sv.setVisibility(View.VISIBLE);
                } else {
                    sv.setVisibility(View.GONE);
                }
            }
        });

        createPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractor = new InsuredInfo(contr_fn.getText().toString(), contr_ln.getText().toString(), contr_ssn.getText().toString(), contr_addr.getText().toString(), contr_pc.getText().toString(), contr_city.getText().toString());
                if (isInsured.isChecked()) {
                    insured = new InsuredInfo(ins_fn.getText().toString(), ins_ln.getText().toString(), ins_ssn.getText().toString(), ins_addr.getText().toString(), ins_pc.getText().toString(), ins_city.getText().toString());
                } else {
                    insured = contractor;
                }

                Log.i("contractor info", contractor.getFirst_name());
                Log.i("insured info", insured.getFirst_name());

                BookTravelPolicy bookTravelPolicy = new BookTravelPolicy();
                bookTravelPolicy.execute();
            }
        });
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.FRANCE);
        start_date.setText(dateFormat.format(myCalendar.getTime()));
        startDate = dateFormat.format(myCalendar.getTime());
    }

    public class BookTravelPolicy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = null;
            PropertyInfo pi = new PropertyInfo();
            switch (typePolicy) {
                case "TRA":
                    request = new SoapObject(Globals.NAMESPACE, Globals.BOOK_TRAVEL);
                    pi.setName("BookTravelInfo");
                    pi.setValue(travelInfo);
                    pi.setType(travelInfo.getClass());
                    break;
                case "HHL":
                    request = new SoapObject(Globals.NAMESPACE, Globals.BOOK_HOUSEHOLD);
                    pi.setName("HouseholdInfo");
                    pi.setValue(householdInfo);
                    pi.setType(householdInfo.getClass());
                    break;
                case "AO":
                    request = new SoapObject(Globals.NAMESPACE, Globals.BOOK_AO);
                    pi.setName("AOInfo");
                    pi.setValue(aoInfo);
                    pi.setType(aoInfo.getClass());
                    break;
                case "CAS":
                    request = new SoapObject(Globals.NAMESPACE, Globals.BOOK_CASCO);
                    pi.setName("CascoInfo");
                    pi.setValue(cascoInfo);
                    pi.setType(cascoInfo.getClass());
                    break;
                case "ACC":
                    request = new SoapObject(Globals.NAMESPACE, Globals.BOOK_ACCIDENT);
                    pi.setName("AccidentInfo");
                    pi.setValue(accidentInfo);
                    pi.setType(accidentInfo.getClass());
                    break;
            }

            request.addProperty(pi);

            if (!typePolicy.equals("AO")) {
                contractor.setProperty(0, contractor.getFirst_name());
                contractor.setProperty(1, contractor.getLast_name());
                contractor.setProperty(2, contractor.getSsn());
                contractor.setProperty(3, contractor.getAddress());
                contractor.setProperty(4, contractor.getPostal_code());
                contractor.setProperty(5, contractor.getCity());

                PropertyInfo pi2 = new PropertyInfo();
                pi2.setName("Owner");
                pi2.setValue(contractor);
                pi2.setType(contractor.getClass());

                request.addProperty(pi2);
            }

            insured.setProperty(0, insured.getFirst_name());
            insured.setProperty(1, insured.getLast_name());
            insured.setProperty(2, insured.getSsn());
            insured.setProperty(3, insured.getAddress());
            insured.setProperty(4, insured.getPostal_code());
            insured.setProperty(5, insured.getCity());

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("Insured");
            pi3.setValue(insured);
            pi3.setType(insured.getClass());

            request.addProperty(pi3);

            request.addProperty("sessionID", sessionID);
            request.addProperty("startDate", startDate);
            Log.i("request data: ", request.getName());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            switch (typePolicy) {
                case "TRA":
                    envelope.addMapping(Globals.NAMESPACE, "BookTravelInfo", travelInfo.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Owner", contractor.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Insured", insured.getClass());
                    break;
                case "HHL":
                    envelope.addMapping(Globals.NAMESPACE, "HouseholdInfo", householdInfo.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Owner", contractor.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Insured", insured.getClass());
                    break;
                case "AO":
                    envelope.addMapping(Globals.NAMESPACE, "AOInfo", travelInfo.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Insured", insured.getClass());
                    break;
                case "CAS":
                    envelope.addMapping(Globals.NAMESPACE, "CascoInfo", cascoInfo.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Insured", insured.getClass());
                    break;
                case "ACC":
                    envelope.addMapping(Globals.NAMESPACE, "AccidentInfo", accidentInfo.getClass());
                    envelope.addMapping(Globals.NAMESPACE, "Insured", insured.getClass());
                    break;
                default:
                    break;
            }

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Globals.URL);
                androidHttpTransport.debug = true;
                switch (typePolicy) {
                    case "TRA":
                        androidHttpTransport.call(Globals.NAMESPACE + Globals.BOOK_TRAVEL, envelope);
                        break;
                    case "HHL":
                        androidHttpTransport.call(Globals.NAMESPACE + Globals.BOOK_HOUSEHOLD, envelope);
                        break;
                    case "AO":
                        androidHttpTransport.call(Globals.NAMESPACE + Globals.BOOK_AO, envelope);
                        break;
                    case "CAS":
                        androidHttpTransport.call(Globals.NAMESPACE + Globals.BOOK_CASCO, envelope);
                        break;
                    case "ACC":
                        androidHttpTransport.call(Globals.NAMESPACE + Globals.BOOK_ACCIDENT, envelope);
                        break;
                    default:
                        break;
                }

                Log.i("request dump:", androidHttpTransport.requestDump);
                Log.i("response dump:", androidHttpTransport.responseDump);

                SoapObject result = (SoapObject) envelope.bodyIn;
                if(result != null) {
                    SoapObject getResponse = (SoapObject) result.getProperty(0);

                    code = getResponse.getPrimitivePropertyAsString("code");
                    policyID = getResponse.getPrimitivePropertyAsString("policyID");
                    message = getResponse.getPrimitivePropertyAsString("message");

                    Log.i("code", code);
                    Log.i("policyID", policyID);
                } else {
                    Toast.makeText(getApplicationContext(), "No response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return policyID;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ONPOSTEX", String.valueOf(result));
            context = getApplicationContext();
            if (code.equals("100")) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ConfirmPolicy.class);
                intent.putExtra("policyID", policyID);
                intent.putExtra("premium", premium);
                intent.putExtra("typePolicyMapped", typePolicyMapped);
                startActivity(intent);
            } else  {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }



}