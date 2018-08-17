package com.example.xghos.Wrenchy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class StartActivity extends FragmentActivity {

    ViewGroup content;
    private LoginFragment loginFragment;
    private String newToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        content = findViewById(R.id.contentPanel);

        Intent intent = getIntent();

        if(intent.getExtras() != null)
            Log.d("extras", intent.getExtras().toString());

        NotificationService notificationService = new NotificationService();
        notificationService.createNotificationChannel();

        if(savedInstanceState == null){
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.contentPanel, loginFragment).commit();
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                loginFragment.token = newToken;
                Log.e("newToken",newToken);

            }
        });
    }

    //TODO https://www.figma.com/file/TSOvrCc69f37bAr2y9lUPSpK/DMT?node-id=1%3A119
}
