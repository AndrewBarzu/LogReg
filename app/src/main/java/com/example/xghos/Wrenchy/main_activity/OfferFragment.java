package com.example.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.ViewPagerAdapter;
import com.example.xghos.Wrenchy.helpers_extras.OfferPicture;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;

import java.util.ArrayList;


public class OfferFragment extends Fragment {

    private String mOfferId;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private ArrayList<String> mOfferImages;
    private ToolbarInterface toolbarInterface;

    public OfferFragment(){

    }

    public static OfferFragment newInstance(String OfferID, String OfferTitle, String OfferDescription, String OfferLocation, String OfferExpire, String OfferPrice, ArrayList<String> OfferImages) {  //TODO also send the title, description, expiration date etc with the id so that we can skip the loading
        OfferFragment fragment = new OfferFragment();
        fragment.mOfferTitle = OfferTitle;
        fragment.mOfferDescription = OfferDescription;
        fragment.mOfferLocation = OfferLocation;
        fragment.mOfferExpire = OfferExpire;
        fragment.mOfferPrice = OfferPrice;
        fragment.mOfferImages = OfferImages;
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
        toolbarInterface.hideToolbar();
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
        OfferPicture offerPicture;
        Drawable drawable = getResources().getDrawable(R.drawable.ic_photo_black_24dp);
        if(mOfferImages.size()>0)
            for (String image : mOfferImages) {
                offerPicture = OfferPicture.newInstance(image, drawable);
                viewPagerAdapter.addFragment(offerPicture);
            }
        else{
            offerPicture = OfferPicture.newInstance(null, drawable);
            viewPagerAdapter.addFragment(offerPicture);
        }
        VPImageList.setAdapter(viewPagerAdapter);
        dots.setupWithViewPager(VPImageList);
        toolbarInterface.setToolbarTitle(R.string.offer_details);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            toolbarInterface = (ToolbarInterface)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ToolbarInterface");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbarInterface.showToolbar();
    }
}
