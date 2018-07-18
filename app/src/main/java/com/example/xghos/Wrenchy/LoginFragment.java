package com.example.xghos.Wrenchy;

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

    /**
     * Fragmentul de login
     */


    private EditText ETMail;               //mailul userului, folosit in logare, trimis prin async task la server
    private EditText ETPassword;           //parola userului, la fel ca mailul
    private Button BLogin;                 //butonul care apare in ui
    private SharedPreferences sharedPrefs; //in care sunt salvatele datele de logare ale utilizatorului, pentru completarea automata a acestora in viitor
    private CheckBox CRemember;            //remember me
    private ConstraintLayout layout;       //layoutul in sine, folosit pentru ascunderea tastaturii prin apasarea acestuia

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {        //functie onCreate, necesara fragmentului
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {    //functia onCreateView, in care se fac toate operatiunile UI
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        container.setAlpha(1F);

        layout = view.findViewById(R.id.LoginPanel);
        layout.setOnClickListener(new View.OnClickListener() {   //listener pentru click in layout, prin click pe layout se ascunde tastatura
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
            public void onClick(View view) {       //trimitere la pagina de register
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out); //animatie de fade in/out
                fragmentTransaction.replace(R.id.contentPanel, new RegisterFragment());
                fragmentTransaction.addToBackStack(null); //adaugare la back stack pentru o accesare mai usoara a fragmentului de login, fara reinitializarea acestuia
                fragmentTransaction.commit();
            }
        });

        ETMail = view.findViewById(R.id.ETEmail);
        ETPassword = view.findViewById(R.id.ETPassword);

        BLogin = view.findViewById(R.id.BLogin);
        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //incercarea de login
                if(Helper.getINSTANCE().loginValidation(ETMail.getText().toString(), ETPassword.getText().toString()))
                    new LoginAsyncTask().execute();
                else
                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        CRemember = view.findViewById(R.id.CRemember);
        sharedPrefs = getContext().getApplicationContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        if (sharedPrefs.contains("Email") && sharedPrefs.contains("Pass")) {   //completarea automata a campurilor email si parola
            ETMail.setText(sharedPrefs.getString("Email", ""));
            ETPassword.setText(sharedPrefs.getString("Pass", ""));
            CRemember.setChecked(true);
        }

        final TextView forgotPassword = view.findViewById(R.id.forgotPass);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {      //trimitere la pagina de forgot password
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.contentPanel, new ForgotPW());
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
                String responseMessage = responseObject.getString("response");
                if(responseMessage.equals("Parola incorecta."))
                    return responseMessage;
                JSONObject Object = responseObject.getJSONObject("result");

                String name = Object.getString("nume");
                String email = Object.getString("email");
                String phone = Object.getString("nr_telefon");

                currentUser.setUserName(name);
                currentUser.setEmail(email);
                currentUser.setPhoneNumber(phone);
                currentUser.setId(Object.getString("id_user"));
                currentUser.setAccType(Object.getString("tip_user"));
                currentUser.setAvatar(Object.getString("avatar"));
                currentUser.setStatus(Object.getString("status"));
                currentUser.setOldpw(password);

                if (message.equals("success"))
                {
                    if (CRemember.isChecked()) {
                        editor.putString("Email", mail);
                        editor.putString("Pass", password);
                        editor.apply();
                    }
                    else
                    {
                        editor.putString("Email", "");
                        editor.putString("Pass", "");
                        editor.apply();
                    }

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                return responseMessage;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return "Unknown Error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "Parola incorecta.":
                    Toast.makeText(getContext(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                    break;
                case "Logare cu succes.":
                    Toast.makeText(getActivity().getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    break;
                case "Unknown Error":
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
