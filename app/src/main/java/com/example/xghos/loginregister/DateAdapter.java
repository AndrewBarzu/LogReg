package com.example.xghos.loginregister;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyHolder> implements TouchListener.StickyHeaderInterface {

    private ArrayList<MyDate> mDates;
    private View prevSelectedItem;

    public DateAdapter(ArrayList<MyDate> dates) {
        mDates = dates;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        return 0;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return 0;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {

    }

    @Override
    public boolean isHeader(int itemPosition) {
        return false;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView date, name;
        public MyHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.day);
            name = view.findViewById(R.id.dayName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(prevSelectedItem == null){
                        prevSelectedItem = v;
                        prevSelectedItem.setClickable(false);
                        date.setTextColor(Color.LTGRAY);
                        name.setTextColor(Color.LTGRAY);
                    }
                    else{
                        v.setClickable(false);
                        date.setTextColor(Color.LTGRAY);
                        name.setTextColor(Color.LTGRAY);
                        prevSelectedItem.setClickable(true);
                        TextView day = prevSelectedItem.findViewById(R.id.day);
                        TextView name = prevSelectedItem.findViewById(R.id.dayName);
                        day.setTextColor(Color.BLACK);
                        name.setTextColor(Color.BLACK);
                        prevSelectedItem = v;
                    }

                }
            });
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.date.setText(String.valueOf(mDates.get(position).getDay()));
        holder.name.setText(mDates.get(position).getDayName());
        Log.d("pokeman", String.valueOf(mDates.get(position).getDay()));
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
