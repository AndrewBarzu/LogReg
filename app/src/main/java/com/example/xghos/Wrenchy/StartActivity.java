package com.example.xghos.Wrenchy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

public class StartActivity extends FragmentActivity {

    /** Activitatea cu care incepe aplicatia. E definita doar variabila content, in care vor fi plasate
     * celelalte fragmente din faza principala, respectiv cel de login, cu care se si deschide activitatea, cel de register
     * si cel de forgot password.
     */

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

    //TODO https://www.figma.com/file/TSOvrCc69f37bAr2y9lUPSpK/DMT?node-id=1%3A119
}
