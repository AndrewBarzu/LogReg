package com.appist.xghos.Wrenchy.helpers_extras;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appist.xghos.Wrenchy.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  interface
 * to handle interaction events.
 * Use the {@link OfferPicture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferPicture extends Fragment {

    private String imageString;
    private ImageView imageView;
    private Drawable drawable;

    public OfferPicture() {
        // Required empty public constructor
    }

    public static OfferPicture newInstance(String image, Drawable resource) {
        OfferPicture fragment = new OfferPicture();
        fragment.imageString = image;
        fragment.drawable = resource;
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
        if (imageString != null)
            imageView.setImageBitmap(Helper.getINSTANCE().getBitmapFromString(imageString));
        else
            imageView.setImageDrawable(drawable);
        return view;
    }
}
