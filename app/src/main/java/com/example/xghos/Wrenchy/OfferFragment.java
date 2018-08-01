package com.example.xghos.Wrenchy;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OfferFragment extends Fragment {

    private String mOfferId;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private String mOfferImage;
    private TextView TVOfferTitle;
    private TextView TVOfferDescription;
    private TextView TVOfferLocation;
    private TextView TVOfferExpire;
    private TextView TVOfferPrice;
    private ViewPager VPImageList;

    public OfferFragment(){

    }

    public static OfferFragment newInstance(String OfferTitle, String OfferDescription, String OfferLocation, String OfferExpire, String OfferPrice, String OfferImage) {  //TODO also send the title, description, expiration date etc with the id so that we can skip the loading
        OfferFragment fragment = new OfferFragment();
        fragment.mOfferTitle = OfferTitle;
        fragment.mOfferDescription = OfferDescription;
        fragment.mOfferLocation = OfferLocation;
        fragment.mOfferExpire = OfferExpire;
        fragment.mOfferPrice = OfferPrice;
        fragment.mOfferImage = OfferImage;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offer_details, container, false);
        ((MainActivity)getActivity()).toolbar.setVisibility(View.GONE);
        TabLayout dots = v.findViewById(R.id.dots);
        TVOfferTitle = v.findViewById(R.id.offerTitle);
        TVOfferDescription = v.findViewById(R.id.offerDetails);
        TVOfferExpire = v.findViewById(R.id.expireDate);
        TVOfferLocation = v.findViewById(R.id.offerLocation);
        TVOfferPrice = v.findViewById(R.id.price);
        VPImageList = v.findViewById(R.id.viewPager);
        TVOfferTitle.setText(mOfferTitle);
        TVOfferDescription.setText(mOfferDescription);
        TVOfferExpire.setText(mOfferExpire);
        TVOfferLocation.setText(mOfferLocation);
        TVOfferPrice.setText(mOfferPrice);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        OfferPicture offerPicture = OfferPicture.newInstance(Helper.getINSTANCE().getBitmapFromString(mOfferImage));
        viewPagerAdapter.addFragment(offerPicture);
        VPImageList.setAdapter(viewPagerAdapter);
        dots.setupWithViewPager(VPImageList);
        TextView toolbarTitle = ((MainActivity)getActivity()).toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.offer_details);
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
