package com.example.insurancemobileapp.wsmethods;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.Globals;
import com.example.insurancemobileapp.R;
import com.example.insurancemobileapp.modelclasses.AccidentInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Accident extends AppCompatActivity {


    final Calendar myCalendar = Calendar.getInstance();
    EditText accident_start_date;
    Button calculate;
    CheckBox isStudent;

    String input_sd;
    boolean input_is;
    int input_p;

    String sessionID;

    String message;
    String code;
    String premium;

    AccidentInfo accidentInfo;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

        accident_start_date = (EditText) findViewById(R.id.accident_start_date);
        isStudent = (CheckBox) findViewById(R.id.isStudent);
        calculate = (Button) findViewById(R.id.calcAccident);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        accident_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Accident.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        SharedPreferences prefs = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStudent.isChecked()) {
                    input_is = true;
                } else {
                    input_is = false;
                }
                sessionID = prefs.getString("sessionid", "12345678");
                GetAccidentQuotation getAccidentQuotation = new GetAccidentQuotation();
                getAccidentQuotation.execute();
            }
        });
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.FRANCE);
        accident_start_date.setText(dateFormat.format(myCalendar.getTime()));
        input_sd = dateFormat.format(myCalendar.getTime());
    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.pack1:
                if (checked)
                    input_p = 1;

                break;
            case R.id.pack2:
                if (checked)
                    input_p = 2;
                break;
            case R.id.pack3:
                if (checked)
                    input_p = 3;
                break;
            default:
                break;
        }
    }

    public class GetAccidentQuotation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(Globals.NAMESPACE, Globals.GET_ACCIDENT);
            accidentInfo = new AccidentInfo();
            accidentInfo.setProperty(0, input_sd);
            accidentInfo.setProperty(1, input_is);
            accidentInfo.setProperty(2, input_p);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("AccidentInfo");
            pi.setValue(accidentInfo);
            pi.setType(accidentInfo.getClass());

            request.addProperty(pi);
            request.addProperty("sessionID", sessionID);
            Log.i("request data: ", request.getName());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(Globals.NAMESPACE, "AccidentInfo", accidentInfo.getClass());

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Globals.URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(Globals.NAMESPACE + Globals.GET_ACCIDENT, envelope);

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
                intent.putExtra("accidentInfo", accidentInfo);
                intent.putExtra("sessionid", sessionID);
                intent.putExtra("premiumAccident", premium);
                intent.putExtra("typePolicy", "ACC");
                startActivity(intent);
            } else  {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}