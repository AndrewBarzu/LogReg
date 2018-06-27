package com.example.xghos.Wrenchy;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder> {

    /*
    *Adaptorul pentru oferte, aici sunt luate ofertele de pe server si puse in listView prin apasarea unei date afisate in calendar
    Momentan apare doar titlul ofertei
     */

    private ArrayList<MyOffer> mOffers;
    private int mResource;
    private Context mContext;

    public OfferAdapter(Context context, int resource, ArrayList<MyOffer> offers) {
        mResource = resource;
        mOffers = offers;
        mContext = context;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public TextView offerName, offerPrice, offerLocation, offerer;
        public String offer_id;
        public MyHolder(View view){
            super(view);
            offerName = view.findViewById(R.id.offerName);
            offerPrice = view.findViewById(R.id.offerPrice);
            offerLocation = view.findViewById(R.id.offerLocation);
            offerer = view.findViewById(R.id.offerorName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.main_content, new OfferFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.offerName.setText(mOffers.get(position).getName());
        holder.offerPrice.setText(mOffers.get(position).getPrice());
        holder.offerLocation.setText(mOffers.get(position).getLocation());
        holder.offerer.setText(mOffers.get(position).getOfferer());
        holder.offer_id = mOffers.get(position).getOffer_id();
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

}
