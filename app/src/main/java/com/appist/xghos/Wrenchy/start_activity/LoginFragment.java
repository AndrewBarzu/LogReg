package com.appist.xghos.Wrenchy.start_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.appist.xghos.Wrenchy.helpers_extras.Helper;
import com.appist.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.appist.xghos.Wrenchy.main_activity.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {

    /**
     * Fragmentul de login
     */

    public String token;
    private EditText ETMail;               //mailul userului, folosit in logare, trimis prin async task la server
    private EditText ETPassword;           //parola userului, la fel ca mailul
    private Button BLogin;                 //butonul care apare in ui
    private SharedPreferences sharedPrefs; //in care sunt salvatele datele de logare ale utilizatorului, pentru completarea automata a acestora in viitor
    private CheckBox CRemember;            //remember me
    private ConstraintLayout layout;       //layoutul in sine, folosit pentru ascunderea tastaturii prin apasarea acestuia
    private ConstraintLayout loadingScreen;
    private Boolean loginTry;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {        //functie onCreate, necesara fragmentului
        super.onCreate(savedInstanceState);
        loginTry = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {    //functia onCreateView, in care se fac toate operatiunile UI
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        if(container != null)
            container.setAlpha(1F);

        loadingScreen = view.findViewById(R.id.loading_screen);

        sharedPrefs = getContext().getApplicationContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        if (!sharedPrefs.getString("Email", "").equals("") && !sharedPrefs.getString("Pass", "").equals("") && !loginTry){
            loadingScreen.setVisibility(View.VISIBLE);
            new LoginAsyncTask().execute(sharedPrefs.getString("Email", ""), sharedPrefs.getString("Pass", ""));
            loginTry = true;
        }

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
                if(Helper.getINSTANCE().loginValidation(ETMail.getText().toString(), ETPassword.getText().toString())) {
                    new LoginAsyncTask().execute(ETMail.getText().toString(), ETPassword.getText().toString());
                    BLogin.setClickable(false);
                    loadingScreen.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        CRemember = view.findViewById(R.id.CRemember);
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

     public class LoginAsyncTask extends AsyncTask<String, Void, String>{

        private String name;
        private String email;
        private String phone;
        private String password;
        private String id;
        private String accType;
        private String avatar;

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            final SharedPreferences.Editor editor = sharedPrefs.edit();

            email = objects[0];
            password = objects[1];

            getParams.put("mail", email);
            getParams.put("parola", password);
            getParams.put("request", "login");
            getParams.put("SO", "ANDROID");
            if(token != null)
                getParams.put("token", token);
            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/login.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                String responseMessage = responseObject.getString("response");
                if(responseMessage.equals("Parola incorecta."))
                    return responseMessage;
                JSONObject Object = responseObject.getJSONObject("result");

                name = Object.getString("nume");
                phone = Object.getString("nr_telefon");
                id = Object.getString("id_user");
                accType = Object.getString("tip_user");
                avatar = Object.getString("avatar");

                if (message.equals("success"))
                {
                    if (CRemember.isChecked()) {
                        editor.putString("Email", objects[0]);
                        editor.putString("Pass", objects[1]);
                        editor.apply();
                    }
                    else
                    {
                        editor.putString("Email", "");
                        editor.putString("Pass", "");
                        editor.apply();
                    }
                    editor.putString("Status", Object.getString("status"));
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
            switch (s) {
                case "Parola incorecta.":
                    Toast.makeText(getContext(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                    loadingScreen.setVisibility(View.GONE);
                    BLogin.setClickable(true);
                    break;
                case "Logare cu succes.":
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", name);
                    bundle.putString("email", email);
                    bundle.putString("phone", phone);
                    bundle.putString("id", id);
                    bundle.putString("accType", accType);
                    bundle.putString("avatar", avatar);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    new CountDownTimer(1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            loadingScreen.setVisibility(View.GONE);
                        }
                    }.start();
                    BLogin.setClickable(true);
                    break;
                case "Unknown Error":
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    loadingScreen.setVisibility(View.GONE);
                    BLogin.setClickable(true);
                    break;
            }
        }
    }
}
