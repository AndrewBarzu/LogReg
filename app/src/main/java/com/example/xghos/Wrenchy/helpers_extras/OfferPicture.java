package com.example.xghos.Wrenchy.helpers_extras;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xghos.Wrenchy.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  interface
 * to handle interaction events.
 * Use the {@link OfferPicture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferPicture extends Fragment {

    private Bitmap imageBitmap;
    private ImageView imageView;

    public OfferPicture() {
        // Required empty public constructor
    }

    public static OfferPicture newInstance(Bitmap image) {
        OfferPicture fragment = new OfferPicture();
        fragment.imageBitmap = image;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_picture, container, false);
        imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);
        return view;
    }
}
