package com.example.xghos.Wrenchy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoReviewsFragment extends Fragment {

    public NoReviewsFragment() {

    }

    public static NoReviewsFragment newInstance() {
        NoReviewsFragment fragment = new NoReviewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_reviews, container, false);
        return v;
    }

}
