package com.example.xghos.loginregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    DBHelper db;
    private SharedPreferences sharedPrefs;
    CheckBox CRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText ETMail = findViewById(R.id.ETEmail);
        final EditText ETPassword = findViewById(R.id.ETPassword);
        final Button BLogin = findViewById(R.id.BLogin);
        final TextView registerLink = findViewById(R.id.TVRegHere);

        CRemember = findViewById(R.id.CRemember);

        sharedPrefs = this.getApplicationContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        if (sharedPrefs.contains("Email") && sharedPrefs.contains("Pass")) {
            ETMail.setText(sharedPrefs.getString("Email", ""));
            ETPassword.setText(sharedPrefs.getString("Pass", ""));
            CRemember.setChecked(true);
        }

        final SharedPreferences.Editor editor = sharedPrefs.edit();

        db = new DBHelper(this);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.this.startActivity(new Intent(Login.this, Register.class));

            }
        });

        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ETMail.getText().toString();
                String pass = ETPassword.getText().toString();

                User currentUser = db.Authenticate(new User(null, null, email, pass, null));

                DBHelper dbh = new DBHelper(getApplicationContext());

                SQLiteDatabase db = dbh.getReadableDatabase();

                String query = "SELECT * FROM MY_TABLE;";

                Cursor cursor = db.rawQuery(query, null);

                while (cursor.moveToNext())
                {
                    Log.d("APPLOG", cursor.getString(cursor.getColumnIndex("USERNAME")) + " " + cursor.getString(cursor.getColumnIndex("PASSWORD")) + " " + cursor.getString(cursor.getColumnIndex("TYPE")));
                }
                Log.d("APPLOG", "");


                if (currentUser != null) {
                    if (CRemember.isChecked()) {
                        editor.putString("Email", email);
                        editor.putString("Pass", pass);
                    }
                    else
                        editor.clear();
                    editor.commit();
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("userName", currentUser.userName);
                    intent.putExtra("accType", currentUser.accType);
                    startActivity(intent);
                } else
                    Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
