package com.example.insurancemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.insurancemobileapp.account.PasswordManager;
import com.example.insurancemobileapp.wsmethods.Accident;
import com.example.insurancemobileapp.wsmethods.Ao;
import com.example.insurancemobileapp.wsmethods.Casco;
import com.example.insurancemobileapp.wsmethods.Household;
import com.example.insurancemobileapp.wsmethods.TravelHealth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private static final String url = "jdbc:mysql://db-mysql-fra1-insurance-do-user-10143551-0.b.db.ondigitalocean.com:25060/defaultdb";
    private static final String user = "doadmin";
    private static final String pass = BuildConfig.DoPass;

    TextView loggedUserText;
    String sessionID;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SharedPreferences prefs = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        Log.i("Home_page", String.valueOf(prefs.getInt("isLogged", 0)) + ", " + prefs.getString("logged_user", "ERROR"));

        loggedUserText = findViewById(R.id.logged_user);
        loggedUserText.setText(prefs.getString("logged_user", "ERROR"));

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String text = ldt.format(formatter);
        Log.i("localdatetime", text);

        Timestamp timestamp = Timestamp.valueOf(text);
        try {
            sessionID = PasswordManager.generateStorngPasswordHash("salt");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        ArrayList sessionParams = new ArrayList<>();
        sessionParams.add(prefs.getString("logged_user", "ERROR")); // username
        sessionParams.add(timestamp);
        sessionParams.add(sessionID);

        MakeSession ms = new MakeSession();
        ms.execute(sessionParams);
    }

    public class MakeSession extends AsyncTask<ArrayList, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(ArrayList... params) {
            ArrayList passed = params[0];
            Log.i("params inside 1", String.valueOf(passed.get(0)));
            Log.i("params inside 2", String.valueOf(passed.get(1)));
            Log.i("params inside 3", String.valueOf(passed.get(2)));

            int result = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database success");
                Statement st = con.createStatement();
                result = st.executeUpdate("insert into sessions(username, from_date, sessionid) values ('" + String.valueOf(passed.get(0))+ "', '" + String.valueOf(passed.get(1)) + "', '" + String.valueOf(passed.get(2)) + "');");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Integer.valueOf(result);
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("ONPOSTEX", String.valueOf(result));
            SharedPreferences preferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("sessionid", sessionID);
            editor.apply();
        }
    }

    public void toTravel(View view) {
        Intent intent = new Intent(this, TravelHealth.class);
        startActivity(intent);
    }

    public void toHousehold(View view) {
        Intent intent = new Intent(this, Household.class);
        startActivity(intent);
    }

    public void toAO(View view) {
        Intent intent = new Intent(this, Ao.class);
        startActivity(intent);
    }

    public void toCasco(View view) {
        Intent intent = new Intent(this, Casco.class);
        startActivity(intent);
    }

    public void toAccident(View view) {
        Intent intent = new Intent(this, Accident.class);
        startActivity(intent);
    }


}