package com.example.insurancemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.account.LoginActivity;
import com.example.insurancemobileapp.wsmethods.TravelHealth;

public class MainActivity extends AppCompatActivity {

    String loggedUser = "";
    TextView loggedUserText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
//
//        if (prefs.getInt("isLogged", 0) == 0) {
//            Log.i("loggedUser", "NOT LOGGED IN");
//        } else if (prefs.getInt("isLogged", 0) == 1){
//            Log.i("loggedUser", "LOGGED IN");
//            loggedUser = prefs.getString("logged_user", null);
//        }
//
//        loggedUserText = (TextView) findViewById(R.id.logged_user);
//
//        if (loggedUser == null) {
//            loggedUserText.setText("Currently not logged in!");
//        } else {
//            loggedUserText.setText("Welcome, " + loggedUser);
//        }
//    }

    public void toLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void toDatabase(View view) {
        Intent intent = new Intent(this, Database.class);
        startActivity(intent);
    }

    public void toWebService(View view) {
        Intent intent = new Intent(this, TravelHealth.class);
        startActivity(intent);
    }
}