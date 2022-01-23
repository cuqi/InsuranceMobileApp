package com.example.insurancemobileapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Database extends AppCompatActivity {

    private static final String url = "jdbc:mysql://db-mysql-fra1-insurance-do-user-10143551-0.b.db.ondigitalocean.com:25060/defaultdb";
    private static final String user = "doadmin";
    private static final String pass = BuildConfig.DoPass;
    Button btnFetch;
    TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        btnFetch = (Button) findViewById(R.id.button2);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // TODO Auto-generated method stub
                Log.i("inside on click", "inside");
//                ConnectMySql connectMySql = new ConnectMySql();
//                connectMySql.execute("");
                String[] userParams = {"cuqi12", "$2a$10$mDOq7vjNODhGTmtO2sExL.TAQEE6IxLzLConz/uM3jdJRc8.8g2ja"};
                CheckUser checkUser = new CheckUser();
                checkUser.execute(userParams);
            }
        });
    }

    public class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Database.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Databaseection success");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select name from users");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    result += rs.getString(1).toString() + "\n";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ONPOSTEX", result);
            //txtData.setText(result);
        }
    }

    public class CheckUser extends AsyncTask<String[], Void, Integer> {
        int res = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Database.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected Integer doInBackground(String[]... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database success");

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select count(*) from users where email = '" + params[0] + "' and password = '" + params[1] + "';");

                while (rs.next()) {
                    res = Integer.valueOf(rs.getInt(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("ONPOSTEX", String.valueOf(result));
        }
    }

    public static class UpdatePolicy extends AsyncTask<String[], Void, Integer> {
        int res = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(Database.this, "Please wait...", Toast.LENGTH_SHORT)
//                    .show();

        }

        @Override
        protected Integer doInBackground(String[]... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database success");

                Statement st = con.createStatement();
                res = st.executeUpdate("update travelPolicy set status = 'CL' where policy_id = 'TRA1320004';");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("ONPOSTEX", String.valueOf(result));
        }
    }
}
