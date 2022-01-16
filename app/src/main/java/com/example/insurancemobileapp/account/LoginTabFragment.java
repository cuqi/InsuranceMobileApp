package com.example.insurancemobileapp.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.insurancemobileapp.BuildConfig;
import com.example.insurancemobileapp.HomePage;
import com.example.insurancemobileapp.R;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginTabFragment extends Fragment {

    private static final String url = "jdbc:mysql://db-mysql-fra1-insurance-do-user-10143551-0.b.db.ondigitalocean.com:25060/defaultdb";
    private static final String user = "doadmin";
    private static final String pass = BuildConfig.DoPass;

    EditText email;
    EditText password;
    TextView forgotPass;
    Button loginButton;
    float v = 0;

    Context context;
    PasswordManager passwordManager;
    String globalPassword;
    String encodedPassword;
    String plainPassword;

    boolean isClicked = false;
    Integer res = new Integer(0);
    String username = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        forgotPass = root.findViewById(R.id.forgot_pass);
        loginButton = root.findViewById(R.id.login_button);

        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPass.setTranslationX(800);
        loginButton.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forgotPass.setAlpha(v);
        loginButton.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_email = email.getText().toString().trim();
                String input_password = password.getText().toString().trim();
                plainPassword = input_password;
                try {
                    encodedPassword = passwordManager.generateStorngPasswordHash(input_password);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

                Log.i("encodedPassword: ", encodedPassword);

                ArrayList<String> input_list = new ArrayList<String>();
                input_list.add(input_email);
                input_list.add(encodedPassword);

                CheckUser cu = new CheckUser();
                cu.execute(input_list);
            }
        });

        if (isClicked)
            return view;
        else
            return root;
    }

    public class CheckUser extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = getContext();
            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            ArrayList<String> passed = params[0];
            Log.i("params inside", passed.get(0) + ", " + passed.get(1));
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database success");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select count(*), name, password from users where email = '" + passed.get(0) + "';");

                while (rs.next()) {
                    res = Integer.valueOf(rs.getString(1));
                    username = rs.getString(2);
                    globalPassword = rs.getString(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("ONPOSTEX", String.valueOf(result));
            Log.i("ALLDATA", "global pass:" + globalPassword + ", encoded_pass: " + encodedPassword);
            if (String.valueOf(result).equals("1")) {
                if (username == null) {
                    Log.i("RESULT BAD", String.valueOf(result));
                    Toast.makeText(context, "Invalid email or password! Please try again!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.i("VALIDATE PASSWORD", String.valueOf(passwordManager.validatePassword(globalPassword, encodedPassword)));
                        Log.i("VALIDATE PASSWORD", String.valueOf(passwordManager.validatePassword(plainPassword, globalPassword)));
                        // TODO
                        if (passwordManager.validatePassword(plainPassword, globalPassword)) {
                            Log.i("MATCHES", globalPassword);
                            Toast.makeText(context, "Correct! Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
                            context = getActivity();
                            SharedPreferences preferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("logged_user", username);
                            editor.putInt("isLogged", 1);
                            editor.apply();

                            Intent intent = new Intent(context, HomePage.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Password is incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }
                isClicked = true;
            } else {
                Log.i("RESULT BAD", String.valueOf(result));
                Toast.makeText(context, "Invalid email!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
