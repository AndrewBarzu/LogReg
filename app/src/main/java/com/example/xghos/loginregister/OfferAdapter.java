package com.example.xghos.loginregister;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OfferAdapter extends ArrayAdapter<Integer> {

    private Context mContext;
    private ArrayList<Integer> mOffers;
    private int mResource;

    public OfferAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public OfferAdapter(Context context, int resource, ArrayList<Integer> offers) {
        super(context, resource, offers);
        mContext = context;
        mOffers = offers;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(mResource, null);
        }
        TextView offerNumber = convertView.findViewById(R.id.offerNumber);
        offerNumber.setText(String.valueOf(mOffers.get(position)));
        return convertView;
    }

    @Override
    public int getCount() {
        return mOffers.size();
    }
}
