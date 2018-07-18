package com.example.xghos.Wrenchy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class OfferFragment extends Fragment {

    private String mOfferId;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private TextView TVOfferTitle;
    private TextView TVOfferDescription;
    private TextView TVOfferLocation;
    private TextView TVOfferExpire;
    private TextView TVOfferPrice;

    public OfferFragment(){

    }

    public static OfferFragment newInstance(String OfferTitle, String OfferDescription, String OfferLocation, String OfferExpire, String OfferPrice) {  //TODO also send the title, description, expiration date etc with the id so that we can skip the loading
        OfferFragment fragment = new OfferFragment();
        fragment.mOfferTitle = OfferTitle;
        fragment.mOfferDescription = OfferDescription;
        fragment.mOfferLocation = OfferLocation;
        fragment.mOfferExpire = OfferExpire;
        fragment.mOfferPrice = OfferPrice;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offer, container, false);
        TVOfferTitle = v.findViewById(R.id.offerTitle);
        TVOfferDescription = v.findViewById(R.id.offerDetails);
        TVOfferExpire = v.findViewById(R.id.expireDate);
        TVOfferLocation = v.findViewById(R.id.offerLocation);
        TVOfferPrice = v.findViewById(R.id.price);
        TVOfferTitle.setText(mOfferTitle);
        TVOfferDescription.setText(mOfferDescription);
        TVOfferExpire.setText(mOfferExpire);
        TVOfferLocation.setText(mOfferLocation);
        TVOfferPrice.setText(mOfferPrice);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
