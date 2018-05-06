package com.example.xghos.loginregister;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class CalendarScrollActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    switchToFragment1();
                    return true;
                case R.id.history:
                    switchToFragment1();
                    return true;
                case R.id.profile:
                    switchToFragment1();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_scroll);

//        Calendar startDate = Calendar.getInstance();
//
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.MONTH, 1);
//
//        mHorizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
//                .range(startDate, endDate)
//                .datesNumberOnScreen(5)
//                .build();
//
//        mHorizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//            @Override
//            public void onDateSelected(Calendar date, int position) {
//
//            }
//        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

}

