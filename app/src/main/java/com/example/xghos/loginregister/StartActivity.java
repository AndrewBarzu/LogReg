package com.example.xghos.loginregister;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

public class StartActivity extends FragmentActivity {

    ViewGroup content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        content = findViewById(R.id.contentPanel);

        if(savedInstanceState == null){
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.contentPanel, loginFragment).commit();
        }
    }
}
