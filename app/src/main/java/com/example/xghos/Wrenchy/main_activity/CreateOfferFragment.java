package com.example.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xghos.Wrenchy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateOfferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateOfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOfferFragment extends Fragment {

    public CreateOfferFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_offer, container, false);

        return v;
    }

}
