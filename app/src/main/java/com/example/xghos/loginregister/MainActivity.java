package com.example.xghos.loginregister;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ArrayList<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.offers:
                                Toast.makeText(MainActivity.this, "Offers", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.history:
                                Toast.makeText(MainActivity.this, "history", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.settings:
                                Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.logout:
                                finish();
                        }

                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });


        new getUsersAsync().execute();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getUsersAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put(MCrypt.getInstance().encryptHex("sk"), MCrypt.getInstance().SECRET_KEY);
            Log.d("+++", "sk: "+MCrypt.getInstance().encryptHex("sk"));
            getParams.put(MCrypt.getInstance().encryptHex("msg"), MCrypt.getInstance().encryptHex("asinfapweklsndioawe"));


            try {
                String response = new HttpRequest(getParams,"http://students.doubleuchat.com/mcrypt_test.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("response");
                Log.d("+++", MCrypt.getInstance().decryptHex(message));
                list = new ArrayList<>();
//                JSONArray jsonArray = responseObject.getJSONArray("array");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject contactObj = jsonArray.getJSONObject(i);
//                    User user = new User();
//                    user.setUserName(contactObj.getString("user"));
//                    Log.d("+++", user.getUserName());
//                    user.setEmail(contactObj.getString("email"));
//                    Log.d("+++", user.getEmail());
//                    list.add(user);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
