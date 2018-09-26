package com.appist.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.adapters.ViewPagerAdapter;
import com.appist.xghos.Wrenchy.helpers_extras.OfferPicture;
import com.appist.xghos.Wrenchy.helpers_extras.ScrollableViewPager;
import com.appist.xghos.Wrenchy.interfaces.ToolbarInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OfferFragment extends Fragment implements OnMapReadyCallback {

    private String mOfferId;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private ArrayList<String> mOfferImages;
    private ToolbarInterface toolbarInterface;
    private GoogleMap mMap;

    public OfferFragment() {

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
        toolbarInterface.showBackButton();
        toolbarInterface.remove_add();

        final ScrollView scrollView = v.findViewById(R.id.offerScrollView);
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        mapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        TabLayout dots = v.findViewById(R.id.dots);
        TextView TVOfferTitle = v.findViewById(R.id.offerTitle);
        TextView TVOfferDescription = v.findViewById(R.id.offerDetails);
        TextView TVOfferExpire = v.findViewById(R.id.expireDate);
        TextView TVOfferLocation = v.findViewById(R.id.offerLocation);
        TextView TVOfferPrice = v.findViewById(R.id.price);
        ScrollableViewPager VPImageList = v.findViewById(R.id.viewPager);
        TVOfferTitle.setText(mOfferTitle);
        TVOfferDescription.setText(mOfferDescription);
        TVOfferExpire.setText(mOfferExpire);
        TVOfferLocation.setText(mOfferLocation);
        TVOfferPrice.setText(mOfferPrice);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        OfferPicture offerPicture;
        VPImageList.setListener(new ScrollableViewPager.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        Drawable drawable = getResources().getDrawable(R.drawable.ic_photo_black_24dp);
        if (mOfferImages.size() > 0)
            for (String image : mOfferImages) {
                offerPicture = OfferPicture.newInstance(image, drawable);
                viewPagerAdapter.addFragment(offerPicture);
            }
        else {
            offerPicture = OfferPicture.newInstance(null, drawable);
            viewPagerAdapter.addFragment(offerPicture);
        }
        VPImageList.setAdapter(viewPagerAdapter);
        dots.setupWithViewPager(VPImageList);
        toolbarInterface.setToolbarTitle(R.string.offer_details);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        List<LatLng> ll = new ArrayList<>();

        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(getContext());
                List<Address> addressList = gc.getFromLocationName(mOfferLocation, 1);
                for (Address a : addressList) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
            } catch (IOException e) {
                Log.e("GEOLOCATION ERROR", e.toString());
            }
        }

        LatLng location = ll.get(0);
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 9.0f));
        Log.d("+++", location.toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            toolbarInterface = (ToolbarInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ToolbarInterface");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_gradient));
        toolbarInterface.hideBackButton();
        toolbarInterface.show_add();
    }
}
