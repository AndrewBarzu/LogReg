package com.example.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xghos.Wrenchy.helpers_extras.Helper;
import com.example.xghos.Wrenchy.helpers_extras.OfferPicture;
import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.ViewPagerAdapter;


public class OfferFragment extends Fragment {

    private String mOfferId;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private String mOfferImage;

    public OfferFragment(){

    }

    public static OfferFragment newInstance(String OfferID, String OfferTitle, String OfferDescription, String OfferLocation, String OfferExpire, String OfferPrice, String OfferImage) {  //TODO also send the title, description, expiration date etc with the id so that we can skip the loading
        OfferFragment fragment = new OfferFragment();
        fragment.mOfferTitle = OfferTitle;
        fragment.mOfferDescription = OfferDescription;
        fragment.mOfferLocation = OfferLocation;
        fragment.mOfferExpire = OfferExpire;
        fragment.mOfferPrice = OfferPrice;
        fragment.mOfferImage = OfferImage;
        fragment.mOfferId = OfferID;
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
        TextView TVOfferTitle = v.findViewById(R.id.offerTitle);
        TextView TVOfferDescription = v.findViewById(R.id.offerDetails);
        TextView TVOfferExpire = v.findViewById(R.id.expireDate);
        TextView TVOfferLocation = v.findViewById(R.id.offerLocation);
        TextView TVOfferPrice = v.findViewById(R.id.price);
        ViewPager VPImageList = v.findViewById(R.id.viewPager);
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
