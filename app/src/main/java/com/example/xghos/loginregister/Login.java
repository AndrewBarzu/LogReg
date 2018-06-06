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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {


    EditText ETMail;
    EditText ETPassword;
    Button BLogin;
    private SharedPreferences sharedPrefs;
    CheckBox CRemember;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETMail = findViewById(R.id.email);
        ETPassword = findViewById(R.id.ETPassword);
        Button BLogin = findViewById(R.id.BLogin);
        final TextView registerLink = findViewById(R.id.TVRegHere);

        layout = findViewById(R.id.LoginPanel);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                }
            }
        });

        CRemember = findViewById(R.id.CRemember);

        sharedPrefs = this.getApplicationContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        if (sharedPrefs.contains("Email") && sharedPrefs.contains("Pass")) {
            ETMail.setText(sharedPrefs.getString("Email", ""));
            ETPassword.setText(sharedPrefs.getString("Pass", ""));
            CRemember.setChecked(true);
        }

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.this.startActivity(new Intent(Login.this, Register.class));

            }
        });


        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.getINSTANCE().loginValidation(ETMail.getText().toString(), ETPassword.getText().toString()))
                    new LoginAsyncTask().execute();
                else
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });


//        BLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = ETMail.getText().toString();
//                String pass = ETPassword.getText().toString();
//
//                User currentUser = db.Authenticate(new User(null, null, email, pass, null));
//
//                DBHelper dbh = new DBHelper(getApplicationContext());
//
//                SQLiteDatabase db = dbh.getReadableDatabase();
//
//                String query = "SELECT * FROM MY_TABLE;";
//
//                Cursor cursor = db.rawQuery(query, null);
//
//                while (cursor.moveToNext())
//                {
//                    Log.d("APPLOG", cursor.getString(cursor.getColumnIndex("USERNAME")) + " " + cursor.getString(cursor.getColumnIndex("PASSWORD")) + " " + cursor.getString(cursor.getColumnIndex("TYPE")));
//                }
//                Log.d("APPLOG", "");
//
//
//                if (currentUser != null) {
//                    if (CRemember.isChecked()) {
//                        editor.putString("Email", email);
//                        editor.putString("Pass", pass);
//                    }
//                    else
//                        editor.clear();
//                    editor.commit();
//                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Login.this, MainActivity.class);
//                    intent.putExtra("userName", currentUser.userName);
//                    intent.putExtra("accType", currentUser.accType);
//                    startActivity(intent);
//                } else
//                    Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    private class LoginAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            String mail = ETMail.getText().toString();
            String password = ETPassword.getText().toString();
            getParams.put("mail", mail);
            getParams.put("parola", password);
            getParams.put("request", "login");

            final SharedPreferences.Editor editor = sharedPrefs.edit();

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/login.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                JSONObject Object = responseObject.getJSONObject("response");

                String name = Object.getString("nume") + " " + Object.getString("prenume");
                String email = Object.getString("email");
                String phone = Object.getString("nr_telefon");

                currentUser.setUserName(name);
                currentUser.setEmail(email);
                currentUser.setPhoneNumber(phone);
                currentUser.setId(Object.getString("id_user"));
                currentUser.setAccType(Object.getString("tip_user"));
                currentUser.setAvatar(Object.getString("avatar"));
                currentUser.setStatus(Object.getString("status"));

                Log.d("+++", String.valueOf(Object));

                if (message.equals("success"))
                {
                    if (CRemember.isChecked()) {
                        editor.putString("Email", mail);
                        editor.putString("Pass", password);
                        editor.commit();
                    }
                    else
                    {
                        editor.putString("Email", "");
                        editor.putString("Pass", "");
                        editor.commit();
                    }

                    Intent intent = new Intent(Login.this, CalendarScrollActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("email", email);
                    bundle.putString("phone", phone);
                    intent.putExtras(bundle);
                    startActivity(intent);
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
