package com.example.xghos.loginregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    /*
    Fragmentul principal, in care se pot vedea ofertele si calendarul din care alegem data
     */

    RecyclerView recyclerView;
    ListView listView;
    LinearLayoutManager layoutManager;
    DateAdapter dateAdapter;
    Calendar mStartDate;
    Calendar mEndDate;
    HeaderItemDecoration itemDecoration;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDate = Calendar.getInstance();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.YEAR, 10);
//        myDates = new ArrayList<>();
//        MyDate FIRST_ITEM = new MyDate();
//        FIRST_ITEM.setDay("0");
//        FIRST_ITEM.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
//        myDates.add(FIRST_ITEM);
//        for (int i = 0; mStartDate.compareTo(mEndDate)<=0; mStartDate.add(Calendar.DAY_OF_YEAR, 1), i++){
//            MyDate date = new MyDate();
//            date.setDay(String.valueOf(mStartDate.get(Calendar.DAY_OF_MONTH)));
//            date.setDayName(mStartDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
//            if(Integer.valueOf(date.getDay()) == 1){
//                MyDate HEADER = new MyDate();
//                HEADER.setDay("0");
//                HEADER.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
//                myDates.add(HEADER);
//            }
//            myDates.add(date);
//        }
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
        listView = view.findViewById(R.id.offers);

        dateAdapter = new DateAdapter(getContext(), mStartDate, mEndDate, listView);  //date adapterul necesita si un listView, in care sunt afisate ofertele depinzand de ziua aleasa
        recyclerView = view.findViewById(R.id.calendar);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dateAdapter);
        itemDecoration = new HeaderItemDecoration(recyclerView, dateAdapter);
        recyclerView.addItemDecoration(itemDecoration);

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

