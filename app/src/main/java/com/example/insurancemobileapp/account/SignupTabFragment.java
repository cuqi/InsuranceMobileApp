package com.example.insurancemobileapp.account;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.insurancemobileapp.BuildConfig;
import com.example.insurancemobileapp.HomePage;
import com.example.insurancemobileapp.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class SignupTabFragment extends Fragment {

    private static final String url = "jdbc:mysql://db-mysql-fra1-insurance-do-user-10143551-0.b.db.ondigitalocean.com:25060/defaultdb";
    private static final String user = "doadmin";
    private static final String pass = BuildConfig.DoPass;

    EditText email;
    EditText username;
    EditText pass1;
    EditText pass2;
    Button signupButton;

    Context context;
    PasswordManager passwordManager;
    float v = 0;
    boolean isClicked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        email = root.findViewById(R.id.email_sign);
        username = root.findViewById(R.id.username_sign);
        pass1 = root.findViewById(R.id.pass1_sign);
        pass2 = root.findViewById(R.id.pass2_sign);
        signupButton = root.findViewById(R.id.signup_button);

        email.setTranslationX(800);
        username.setTranslationX(800);
        pass1.setTranslationX(800);
        pass2.setTranslationX(800);
        signupButton.setTranslationX(800);

        email.setAlpha(v);
        username.setAlpha(v);
        pass1.setAlpha(v);
        pass2.setAlpha(v);
        signupButton.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        pass1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        pass2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signupButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        View view = inflater.inflate(R.layout.signup_tab_fragment, container, false);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_email = email.getText().toString().trim();
                String input_username = username.getText().toString().trim();
                String input_pass1 = pass1.getText().toString().trim();
                String input_pass2 = pass2.getText().toString().trim();

                context = getContext();
                if (!(input_pass1.equals(input_pass2))) {
                    Toast.makeText(context, "Passwords don't match", Toast.LENGTH_LONG).show();
                    return;
                }
                ArrayList<String> input_list = new ArrayList<String>();
                input_list.add(input_email);
                input_list.add(input_username);
                input_list.add(input_pass1);

                InsertUser iu = new InsertUser();
                iu.execute(input_list);
            }
        });

        if (isClicked)
            return view;
        else
            return root;
    }

    public class InsertUser extends AsyncTask<ArrayList<String>, Void, Integer> {

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
            Log.i("params inside", passed.get(0) + ", " + passed.get(1) + ", " + passed.get(2));
            int result = 0;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Database success");
                Statement st = con.createStatement();
                String encodedPassword = passwordManager.generateStorngPasswordHash(passed.get(2));
                result = st.executeUpdate("insert into users(email, name, password) values ('" + passed.get(0)+ "', '" + passed.get(1) + "', '" + encodedPassword + "');");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Integer.valueOf(result);
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("ONPOSTEX", String.valueOf(result));

            if (String.valueOf(result).equals("1")) {
                Intent intent = new Intent(context, HomePage.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "An error has occured!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
