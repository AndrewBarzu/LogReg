package com.example.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xghos.Wrenchy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;

    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (savedInstanceState == null) {
            final ScrollView scrollView = v.findViewById(R.id.details_scroll_view);
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.details_map);
            mapFragment.getMapAsync(this);
            mapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
            });
            TextView phone = v.findViewById(R.id.phone_details);
            phone.setText(getActivity().getIntent().getExtras().getString("phone"));
            TextView mail = v.findViewById(R.id.mail_details);
            mail.setText(getActivity().getIntent().getExtras().getString("email"));
            TextView location = v.findViewById(R.id.location_details);
            location.setText("Galati");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(mMap == null) {
            mMap = googleMap;

            List<LatLng> ll = new ArrayList<>();

            if (Geocoder.isPresent()) {
                try {
                    Geocoder gc = new Geocoder(getContext());
                    List<Address> addressList = gc.getFromLocationName("Galati", 1);
                    for (Address a : addressList) {
                        if (a.hasLatitude() && a.hasLongitude()) {
                            ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        }
                    }
                } catch (IOException e) {
                    Log.e("GEOLOCATION ERROR", e.toString());
                }
            }

            if (ll.size() != 0) {
                LatLng location = ll.get(0);
                mMap.addMarker(new MarkerOptions().position(location));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));
            }
        }
    }

}
