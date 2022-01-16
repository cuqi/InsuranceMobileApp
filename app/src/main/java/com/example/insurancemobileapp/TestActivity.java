package com.example.insurancemobileapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class TestActivity extends AppCompatActivity {

    EditText myNum;
    String numValue;
    Button calculate;
    String calcResult;

    Context context;

    private static String SOAP_ACTION1 = "http://myservice/calculate";
    private static String NAMESPACE = "http://myservice/";
    private static String METHOD_NAME1 = "calculate";
    private static String URL = "http://10.0.2.2:8000/myservice?wsdl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        myNum = (EditText) findViewById(R.id.my_num);
        calculate = (Button) findViewById(R.id.meth_calculate);
        numValue = myNum.getText().toString();
        Log.i("num is", numValue);

        calculate.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {


                    Calculate calculate = new Calculate();
                    calculate.execute(numValue);
                }

        });

    }


    public class Calculate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(Globals.NAMESPACE, METHOD_NAME1);
            TestClass testClass = new TestClass();
            testClass.setProperty(0, 2);
            testClass.setProperty(1, "Yes");
            PropertyInfo pi = new PropertyInfo();
            pi.setName("testClass");
            pi.setValue(testClass);
            pi.setType(testClass.getClass());
            request.addProperty(pi);
            Log.i("request data: ", request.getName());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(Globals.NAMESPACE, "testClass", testClass.getClass());

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(SOAP_ACTION1, envelope);

                Log.i("request dump", androidHttpTransport.requestDump);
                Log.i("response dump", androidHttpTransport.responseDump);

                SoapPrimitive res_xml = (SoapPrimitive) envelope.getResponse();
                String res_string = res_xml.toString();
                Log.i("res_string", res_string);
                SoapObject result = (SoapObject) envelope.bodyIn;

                if(result != null) {
                    calcResult = result.getProperty(0).toString();
                    Log.i("RESULT CALC", calcResult);
                } else {
                    //Toast.makeText(getApplicationContext(), "No response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return calcResult;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ONPOSTEX", String.valueOf(result));

        }
    }
}