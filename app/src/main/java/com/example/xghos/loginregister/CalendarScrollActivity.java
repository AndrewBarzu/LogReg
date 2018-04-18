package com.example.xghos.loginregister;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class CalendarScrollActivity extends AppCompatActivity {

    private TextView mTextMessage;

    public static ArrayList<MyDate> dayList;
    public static DateAdapter dateAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Tab1:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.Tab2:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.Tab3:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_scroll);

        ListView lv = findViewById(R.id.dayList);

        Calendar date = Calendar.getInstance();

        for (Calendar i = date; i.get(Calendar.DAY_OF_MONTH) <= i.getActualMaximum(Calendar.DAY_OF_MONTH); i.add(Calendar.DAY_OF_MONTH, 1)) {
            MyDate day = new MyDate();
            day.setDay(String.valueOf(i.get(Calendar.DAY_OF_MONTH)));
            Log.d("+++", day.getDay()+" "+day.getDayName());
            dayList.add(day);
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}

