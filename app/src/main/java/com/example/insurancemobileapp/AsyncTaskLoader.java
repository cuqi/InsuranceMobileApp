//package com.example.insurancemobileapp;
//
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
//
//public class AsyncTaskLoader {
//
//    private static String SOAP_ACTION1 = "http://myservice/calculate";
//    private static String NAMESPACE = "http://myservice/";
//    private static String METHOD_NAME1 = "calculate";
//    private static String URL = "http://10.0.2.2:8000/myservice?wsdl";
//
//    public class Calculate extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
//            // TODO
//            request.addProperty("myNum", "2");
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
//            try {
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//                androidHttpTransport.call(SOAP_ACTION1, envelope);
//                SoapObject result = (SoapObject) envelope.bodyIn;
//                if(result != null) {
//                    calcResult = result.getProperty(0).toString();
//                    Log.i("RESULT CALC", calcResult);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No response", Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return calcResult;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.i("ONPOSTEX", String.valueOf(result));
//
//        }
//}
