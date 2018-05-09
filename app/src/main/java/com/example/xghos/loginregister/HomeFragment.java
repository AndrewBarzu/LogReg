package com.example.xghos.loginregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment {
    HorizontalCalendar mHorizontalCalendar;
    Calendar mStartDate;
    Calendar mEndDate;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDate = Calendar.getInstance();

        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.MONTH, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHorizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(mStartDate, mEndDate)
                .datesNumberOnScreen(5)
                .build();

        mHorizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return super.onDateLongClicked(date, position);
            }
        });
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

