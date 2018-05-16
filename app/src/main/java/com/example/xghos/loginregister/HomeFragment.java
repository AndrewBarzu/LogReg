package com.example.xghos.loginregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DateAdapter dateAdapter;
    Calendar mStartDate;
    Calendar mEndDate;
    ArrayList<MyDate> myDates;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDate = Calendar.getInstance();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.MONTH, 1);
        myDates = new ArrayList<>();
        for (int i = 0; mStartDate.compareTo(mEndDate)<=0; mStartDate.add(Calendar.DAY_OF_YEAR, 1), i++){
            MyDate date = new MyDate();
            date.setDay(String.valueOf(mStartDate.get(Calendar.DAY_OF_MONTH)));
            date.setDayName(mStartDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            myDates.add(date);
            Log.d("papagal", String.valueOf(myDates.get(i).getDay()));
        }
        dateAdapter = new DateAdapter(myDates);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View item = inflater.inflate(R.layout.fragment_home, container, false);
        return item;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.calendar);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dateAdapter);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

}

