package com.example.xghos.loginregister;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;


public class ForgotPW extends Fragment {

    private TextView mOldPass;
    private TextView mNewPass;
    private TextView mConPass;
    private Button mChangePass;

    public ForgotPW() {
        // Required empty public constructor
    }
    public static ForgotPW newInstance() {
        ForgotPW fragment = new ForgotPW();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_pw, container, false);
        mOldPass = view.findViewById(R.id.ETOpass);
        mNewPass = view.findViewById(R.id.ETNpass);
        mConPass = view.findViewById(R.id.ETCpass);

        mChangePass = view.findViewById(R.id.BChangePass);
        mChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.getINSTANCE().changePasswordValidation(mOldPass.getText().toString(), mNewPass.getText().toString(),
                        mConPass.getText().toString(), getContext())){
                    new ChangePassword().execute();
                }
            }
        });
        return view;
    }
    private class ChangePassword extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("oldpw", mOldPass.getText().toString());
            getParams.put("newpw", mNewPass.getText().toString());
            getParams.put("id_user", String.valueOf(currentUser.getId()));
            getParams.put("request", "forgotpw");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/forgotpw.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                String Object = responseObject.getString("response");

                if (message.equals("error"))
                {
                    return Object;
                }

            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("ok")){
                Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();
                //TODO transaction for calendar fragment
                Intent intent = new Intent(getActivity(), CalendarScrollActivity.class);
                currentUser.setStatus("1");
                getActivity().startActivity(intent);
            }
            else {
                Log.d("+++", s);
                Toast.makeText(getActivity(), "Ples tri agan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
