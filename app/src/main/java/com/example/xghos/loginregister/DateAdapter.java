package com.example.xghos.loginregister;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.Inflater;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyHolder> implements HeaderItemDecoration.StickyHeaderInterface{
    private ArrayList<MyDate> mDates;
    private View prevSelectedItem;

    public DateAdapter(Context context, Calendar mStartDate, Calendar mEndDate) {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        mDates = new ArrayList<>();
        MyDate FIRST_ITEM = new MyDate();
        FIRST_ITEM.setDay("0");
        FIRST_ITEM.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase());
        mDates.add(FIRST_ITEM);
        for (int i = 0; mStartDate.compareTo(mEndDate)<=0; mStartDate.add(Calendar.DAY_OF_YEAR, 1), i++){
            MyDate date = new MyDate();
            date.setDay(String.valueOf(mStartDate.get(Calendar.DAY_OF_MONTH)));
            date.setDayName(mStartDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            date.setMonth(String.valueOf(mStartDate.get(Calendar.MONTH)));
            date.setYear(String.valueOf(mStartDate.get(Calendar.YEAR)));
            if(Integer.valueOf(date.getDay()) == 1){
                MyDate HEADER = new MyDate();
                HEADER.setDay("0");
                HEADER.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase());
                mDates.add(HEADER);
            }
            mDates.add(date);
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(Integer.valueOf(mDates.get(position).getDay())== 0)
            return 0;
        return position;
    }


    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.header_item;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        String month = mDates.get(headerPosition).getMonth();
        month = month.substring(0, 3);
        header.setBackgroundColor(Color.RED);
        TextView TVMonth = header.findViewById(R.id.month);
        TVMonth.setText(month);
    }

    @Override
    public boolean isHeader(int itemPosition) {
        if (getItemViewType(itemPosition)==0){
            return true;
        }
        return false;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView date, name, month;
        public MyHolder(View view) {
            super(view);
            date = view.findViewById(R.id.day);
            name = view.findViewById(R.id.dayName);
            month = view.findViewById(R.id.month);

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
                        name.setTextColor(Color.DKGRAY);
                        prevSelectedItem = v;
                    }
                }
            });
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType==0) {
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_item, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);
        }
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(getItemViewType(position)==0){
            holder.month.setText(mDates.get(position).getMonth().substring(0, 3));
            holder.itemView.setBackgroundColor(Color.RED);
            holder.itemView.setClickable(false);
        }
        else {
            holder.date.setText(String.valueOf(mDates.get(position).getDay()));
            holder.name.setText(mDates.get(position).getDayName());
            Log.d("pokeman", String.valueOf(mDates.get(position).getDay()));
        }
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
