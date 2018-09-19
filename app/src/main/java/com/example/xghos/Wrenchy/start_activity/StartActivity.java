package com.example.xghos.Wrenchy.start_activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.services.NotificationService;
import com.example.xghos.Wrenchy.start_activity.LoginFragment;
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

        createNotificationChannel();

        Intent intent = getIntent();

        if(intent.getExtras() != null)
            Log.d("extras", intent.getExtras().toString());

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    //TODO https://www.figma.com/file/TSOvrCc69f37bAr2y9lUPSpK/DMT?node-id=1%3A119
}
