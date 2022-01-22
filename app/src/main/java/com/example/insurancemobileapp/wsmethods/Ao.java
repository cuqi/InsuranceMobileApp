package com.example.insurancemobileapp.wsmethods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.Globals;
import com.example.insurancemobileapp.R;
import com.example.insurancemobileapp.modelclasses.AOInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Ao extends AppCompatActivity {

    EditText regNum;
    EditText chassis;
    EditText kw;
    CheckBox isNew;
    EditText driver_ssn;
    Button calculate;

    String input_rn;
    String input_c;
    int input_k;
    boolean input_in;
    String input_ssn;

    String sessionID;

    AOInfo aoInfo;

    String code;
    String premium;
    String message;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ao);
        SharedPreferences prefs = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        regNum = (EditText) findViewById(R.id.regNum);
        chassis = (EditText) findViewById(R.id.chassis);
        kw = (EditText) findViewById(R.id.kw);
        isNew = (CheckBox) findViewById(R.id.isNew);
        driver_ssn = (EditText) findViewById(R.id.driver_ssn);

        calculate = (Button) findViewById(R.id.calcAO);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_rn = regNum.getText().toString();
                input_c = chassis.getText().toString();
                input_k = Integer.valueOf(kw.getText().toString());
                input_ssn = driver_ssn.getText().toString();
                if (isNew.isChecked()) {
                    input_in = true;
                } else {
                    input_in = false;
                }
                sessionID = prefs.getString("sessionID", "12345678");

                GetAOQuotation getAOQuotation = new GetAOQuotation();
                getAOQuotation.execute();
            }
        });

    }

    public class GetAOQuotation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(Globals.NAMESPACE, Globals.GET_AO);
            aoInfo = new AOInfo();
            aoInfo.setProperty(0, input_rn);
            aoInfo.setProperty(1, input_c);
            aoInfo.setProperty(2, input_k);
            aoInfo.setProperty(3, input_in);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("AOInfo");
            pi.setValue(aoInfo);
            pi.setType(aoInfo.getClass());
            request.addProperty(pi);
            request.addProperty("ssn", input_ssn);
            request.addProperty("sessionID", sessionID);
            Log.i("request data: ", request.getName());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(Globals.NAMESPACE, "AOInfo", aoInfo.getClass());

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Globals.URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(Globals.NAMESPACE + Globals.GET_AO, envelope);

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
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, BookTravelHealth.class);
                intent.putExtra("aoInfo", aoInfo);
                intent.putExtra("sessionid", sessionID);
                intent.putExtra("premiumAO", premium);
                intent.putExtra("typePolicy", "AO");
                startActivity(intent);
            } else  {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}