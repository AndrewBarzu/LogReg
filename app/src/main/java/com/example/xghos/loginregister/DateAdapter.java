package com.example.xghos.loginregister;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DateAdapter extends ArrayAdapter {

    private Context mContext;
    private List<MyDate> mDates;

    public DateAdapter(@NonNull Context context, int resource, List<MyDate> dates) {
        super(context, resource, dates);
        mContext = context;
        mDates = dates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;

        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rootView = inflater.inflate(R.layout.date_item, null);
        }

        final MyDate date = this.mDates.get(position);
        TextView Date = rootView.findViewById(R.id.day);
        TextView Name = rootView.findViewById(R.id.dayName);

        return rootView;
    }
}
