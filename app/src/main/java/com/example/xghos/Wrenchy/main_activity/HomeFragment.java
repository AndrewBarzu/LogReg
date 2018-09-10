package com.example.xghos.Wrenchy.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xghos.Wrenchy.helpers_extras.HeaderItemDecoration;
import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.DateAdapter;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;

import java.util.Calendar;


public class HomeFragment extends Fragment {

    /*
    Fragmentul principal, in care se pot vedea ofertele si calendarul din care alegem data
     */

    private RecyclerView recyclerView;
    private RecyclerView offerList;
    private LinearLayoutManager layoutManager;
    private DateAdapter dateAdapter;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private HeaderItemDecoration itemDecoration;
    private View item;
    private ToolbarInterface toolbarInterface;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarInterface = (ToolbarInterface) getContext();
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.YEAR, 5);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(item == null) {
            item = inflater.inflate(R.layout.fragment_home, container, false);
            offerList = item.findViewById(R.id.offers);
            recyclerView = item.findViewById(R.id.calendar);
            SwipeRefreshLayout swipeRefreshLayout = item.findViewById(R.id.home_refresh_layout);
            dateAdapter = new DateAdapter(getActivity().getIntent().getExtras().getString("id"), getContext(), mStartDate, mEndDate, offerList, swipeRefreshLayout);
            recyclerView.setAdapter(dateAdapter);
            recyclerView.setLayoutManager(layoutManager);
            itemDecoration = new HeaderItemDecoration(recyclerView, dateAdapter);
            recyclerView.addItemDecoration(itemDecoration);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new DateAdapter.GetOffersAsync(dateAdapter).execute();
                }
            });
        }
        return item;
    }

}

