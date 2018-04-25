package com.example.xghos.loginregister;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class CalendarScrollActivity extends AppCompatActivity {

    private TextView mTextMessage;

    public ArrayList<MyDate> dayList;
    public DateAdapter dateAdapter;
    public RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;

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

        mRecyclerView = findViewById(R.id.recyclerView);
        dayList = new ArrayList<>();

        Calendar date = Calendar.getInstance();

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        for(int i = 1; i<=30; i++) {
            MyDate day = new MyDate();
            day.setDay(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
            day.setDayName(date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            date.add(Calendar.DAY_OF_YEAR, 1);
            Log.d("+++", day.getDay()+" "+day.getDayName());
            dayList.add(day);
        }

        dateAdapter = new DateAdapter(this, dayList);
        mRecyclerView.setAdapter(dateAdapter);
        

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}

