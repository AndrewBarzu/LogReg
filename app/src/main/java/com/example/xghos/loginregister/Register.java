package com.example.xghos.loginregister;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class Register extends AppCompatActivity {

    DBHelper db;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText ETName = findViewById(R.id.ETName);
        final EditText ETMail = findViewById(R.id.ETEmail);
        final EditText ETPassword = findViewById(R.id.ETPassword);
        final EditText ETConPassword = findViewById(R.id.ETConPassword);
        final Switch SType = findViewById(R.id.switch1);

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

        db = new DBHelper(this);

        BRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ETName.getText().toString();
                String mail = ETMail.getText().toString();
                String pass = ETPassword.getText().toString();
                String conpass = ETConPassword.getText().toString();
                String accType = "0";
                if (SType.isChecked())
                {
                    accType = "1";
                }

                if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || conpass.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!(mail.contains("@") && mail.contains("."))) {
                    Toast.makeText(Register.this, "Your Email address is not valid!", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(Register.this, "Your Password is too short!", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(conpass)) {
                    Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {

                    db.addUser(new User(null, name, mail, pass, accType));
                    finish();
                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}

