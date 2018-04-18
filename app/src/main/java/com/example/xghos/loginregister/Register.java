package com.example.xghos.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.jar.Attributes;


public class Register extends AppCompatActivity {

    ConstraintLayout layout;

    EditText ETName;
    EditText ETSurName;
    EditText ETMail;
    EditText ETPassword;
    EditText ETConPassword;
    EditText ETPhone;
    Switch SType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ETName = findViewById(R.id.ETName);
        ETSurName = findViewById(R.id.ETSurName);
        ETMail = findViewById(R.id.ETEmail);
        ETPassword = findViewById(R.id.ETPassword);
        ETConPassword = findViewById(R.id.ETConPassword);
        ETPhone = findViewById(R.id.ETPhone);
        SType = findViewById(R.id.switch1);

        final Button BRegister = findViewById(R.id.BRegister);

        layout = findViewById(R.id.RegisterPanel);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                }
            }
        });

        BRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterAsyncTask().execute();
            }
        });

//        BRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = ETName.getText().toString();
//                String mail = ETMail.getText().toString();
//                String pass = ETPassword.getText().toString();
//                String conpass = ETConPassword.getText().toString();
//                String accType = "0";
//                if (SType.isChecked())
//                {
//                    accType = "1";
//                }
//
//                if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || conpass.isEmpty()) {
//                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//                } else if (!(mail.contains("@") && mail.contains("."))) {
//                    Toast.makeText(Register.this, "Your Email address is not valid!", Toast.LENGTH_SHORT).show();
//                } else if (pass.length() < 6) {
//                    Toast.makeText(Register.this, "Your Password is too short!", Toast.LENGTH_SHORT).show();
//                } else if (!pass.equals(conpass)) {
//                    Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    db.addUser(new User(null, name, mail, pass, accType));
//                    finish();
//                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }
    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            String name = ETName.getText().toString();
            String surname = ETSurName.getText().toString();
            String mail = ETMail.getText().toString();
            String password = ETPassword.getText().toString();
            String phone = ETPhone.getText().toString();
            String accType = "2";
            if(SType.isChecked()==true)
                accType = "1";
            getParams.put("nume", name);
            getParams.put("prenume", surname);
            getParams.put("mail", mail);
            getParams.put("parola", password);
            getParams.put("telefon", phone);
            getParams.put("tip", accType);
            getParams.put("request", "register");


            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/register.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("response");
                Log.d("+++", message);
                if (message.equals("Succes."))
                {
                    finish();
                }

            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }
    }
}

