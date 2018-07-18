package com.example.xghos.Wrenchy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;


public class HomeFragment extends Fragment {

    /*
    Fragmentul principal, in care se pot vedea ofertele si calendarul din care alegem data
     */

    RecyclerView recyclerView;
    RecyclerView offerList;
    LinearLayoutManager layoutManager;
    DateAdapter dateAdapter;
    Calendar mStartDate;
    Calendar mEndDate;
    HeaderItemDecoration itemDecoration;
    View item;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.YEAR, 5);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);

//        mStartDate = Calendar.getInstance();
//        mEndDate = Calendar.getInstance();
//        mEndDate.add(Calendar.YEAR, 10);

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
        if(item == null) {
            item = inflater.inflate(R.layout.fragment_home, container, false);
            offerList = item.findViewById(R.id.offers);
            dateAdapter = new DateAdapter(getContext(), mStartDate, mEndDate, offerList);
            recyclerView = item.findViewById(R.id.calendar);
            recyclerView.setAdapter(dateAdapter);
            recyclerView.setLayoutManager(layoutManager);
            itemDecoration = new HeaderItemDecoration(recyclerView, dateAdapter);
            recyclerView.addItemDecoration(itemDecoration);
        }
        return item;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

