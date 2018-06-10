package com.example.xghos.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {


    EditText ETMail;
    EditText ETPassword;
    Button BLogin;
    private SharedPreferences sharedPrefs;
    CheckBox CRemember;
    ConstraintLayout layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        container.setAlpha(1F);

        layout = view.findViewById(R.id.LoginPanel);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                }
            }
        });

        final TextView registerLink = view.findViewById(R.id.TVRegHere);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentPanel, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ETMail = view.findViewById(R.id.ETEmail);
        ETPassword = view.findViewById(R.id.ETPassword);

        BLogin = view.findViewById(R.id.BLogin);
        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.getINSTANCE().loginValidation(ETMail.getText().toString(), ETPassword.getText().toString()))
                    new LoginAsyncTask().execute();
                else
                    Toast.makeText(getContext(), "LoginFragment Failed", Toast.LENGTH_SHORT).show();
            }
        });

        CRemember = view.findViewById(R.id.CRemember);
        sharedPrefs = getContext().getApplicationContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        if (sharedPrefs.contains("Email") && sharedPrefs.contains("Pass")) {
            ETMail.setText(sharedPrefs.getString("Email", ""));
            ETPassword.setText(sharedPrefs.getString("Pass", ""));
            CRemember.setChecked(true);
        }

        final TextView forgotPassword = view.findViewById(R.id.forgotPass);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentPanel, new ForgotedPW());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_login);
//
//        ETMail = findViewById(R.id.email);
//        ETPassword = findViewById(R.id.ETPassword);
//        Button BLogin = findViewById(R.id.BLogin);
//        final TextView registerLink = findViewById(R.id.TVRegHere);
//
//        layout = findViewById(R.id.LoginPanel);
//
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
//                }
//            }
//        });
//
//        CRemember = findViewById(R.id.CRemember);
//
//        sharedPrefs = this.getApplicationContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
//
//        if (sharedPrefs.contains("Email") && sharedPrefs.contains("Pass")) {
//            ETMail.setText(sharedPrefs.getString("Email", ""));
//            ETPassword.setText(sharedPrefs.getString("Pass", ""));
//            CRemember.setChecked(true);
//        }
//
//        registerLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginFragment.this.startActivity(new Intent(LoginFragment.this, RegisterFragment.class));
//
//            }
//        });
//
//
//        BLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Helper.getINSTANCE().loginValidation(ETMail.getText().toString(), ETPassword.getText().toString()))
//                    new LoginAsyncTask().execute();
//                else
//                    Toast.makeText(LoginFragment.this, "LoginFragment Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
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
//                    Toast.makeText(LoginFragment.this, "LoginFragment Successful!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginFragment.this, MainActivity.class);
//                    intent.putExtra("userName", currentUser.userName);
//                    intent.putExtra("accType", currentUser.accType);
//                    startActivity(intent);
//                } else
//                    Toast.makeText(LoginFragment.this, "LoginFragment Failed!", Toast.LENGTH_SHORT).show();
//            }
//        });
//  }
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

                    Intent intent = new Intent(getContext(), CalendarScrollActivity.class);
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
