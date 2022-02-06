package com.example.insurancemobileapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.account.PasswordManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmPolicy extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
    // 192.168.1.6 If you are testing in real device with usb connected to same network then use your IP address
    private static final String BACKEND_URL = "http://10.0.2.2:4242/"; //4242 is port mentioned in server i.e index.js
    EditText amountText;
    CardInputWidget cardInputWidget;
    Button payButton;
    TextView tv_policyID;

    // we need paymentIntentClientSecret to start transaction
    private String paymentIntentClientSecret;
    //declare stripe
    private Stripe stripe;

    Double amountDouble = null;

    private OkHttpClient httpClient;

    static ProgressDialog progressDialog;

    String policyID;
    Double premium;
    String typePolicy;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_policy);
        amountText = findViewById(R.id.amount_id);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        payButton = findViewById(R.id.payButton);
        tv_policyID = (TextView) findViewById(R.id.tv_policy_id);

        Intent intent = getIntent();
        policyID = intent.getStringExtra("policyID");
        premium = Double.parseDouble(intent.getStringExtra("premium"));
        typePolicy = intent.getStringExtra("typePolicyMapped");
        if (premium == null) {
            premium = Double.parseDouble("1000");
        }

        amountText.setText(String.valueOf(premium));
        amountText.setEnabled(false);

        tv_policyID.setText(tv_policyID.getText() + policyID);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Transaction in progress");
        progressDialog.setCancelable(false);
        httpClient = new OkHttpClient();

        //Initialize
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51KDyGFLGs7WA7UIlGQXIGFQBL9iEzy50xTc1LlzXuZiXxr8pLJxWkUHfERGifdS74MX4eZzYVBJ6mCc42mf9DzrR00yyEMgxGx")
        );


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Amount
                //amountDouble = Double.valueOf(amountText.getText().toString());
                amountDouble = premium;
                //call checkout to get paymentIntentClientSecret key
                progressDialog.show();
                startCheckout();
            }
        });
    }

    private void startCheckout() {
        {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\"usd\","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                + "}";
            double amount=amountDouble*100;
            Map<String,Object> payMap=new HashMap<>();
            Map<String,Object> itemMap=new HashMap<>();
            List<Map<String,Object>> itemList =new ArrayList<>();
            payMap.put("currency", "MKD");
            itemMap.put("id", policyID);
            itemMap.put("amount", amount);
            itemList.add(itemMap);
            payMap.put("items", itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "create-payment-intent")
                    .post(body)
                    .build();
            httpClient.newCall(request)
                    .enqueue(new PayCallback(this));

        }
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<ConfirmPolicy> activityRef;
        PayCallback(@NonNull ConfirmPolicy activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            progressDialog.dismiss();
            final ConfirmPolicy activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final ConfirmPolicy activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");

        //once you get the payment client secret start transaction
        //get card detail
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            //now use paymentIntentClientSecret to start transaction
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
            //start payment
            stripe.confirmPayment(ConfirmPolicy.this, confirmParams);
        }
        Log.i("TAG", "onPaymentSuccess: "+paymentIntentClientSecret);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<ConfirmPolicy> activityRef;
        PaymentResultCallback(@NonNull ConfirmPolicy activity) {
            activityRef = new WeakReference<>(activity);
        }
        //If Payment is successful
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            progressDialog.dismiss();
            final ConfirmPolicy activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast toast =Toast.makeText(activity, "Успешно плаќање", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                String uniqueRef = "//";
                try {
                    uniqueRef = PasswordManager.generateStorngPasswordHash("uniquerefnum").split(":")[1].toUpperCase();
                    Log.i("uniqueRef 1", uniqueRef);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                String[] updateData = {typePolicy, policyID, uniqueRef};
                Database.UpdatePolicy updatePolicy;
                updatePolicy = new Database.UpdatePolicy();
                updatePolicy.execute(updateData);

                context = getApplicationContext();
                Intent intent = new Intent(context, HomePage.class);
                startActivity(intent);
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Неуспешно беше плаќањето",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        //If Payment is not successful
        @Override
        public void onError(@NonNull Exception e) {
            progressDialog.dismiss();
            final ConfirmPolicy activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
}