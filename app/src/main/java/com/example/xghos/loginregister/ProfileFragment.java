package com.example.xghos.loginregister;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

import static android.support.v4.graphics.TypefaceCompatUtil.getTempFile;


public class ProfileFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PHONE = "phone";

    private String mName;
    private String mEmail;
    private String mPhone;
    private TextView TVName;
    private TextView TVEmail;
    private TextView TVPhone;
    private ImageView IVProfilePic;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String name, String email, String phone) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_PHONE, phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mEmail = getArguments().getString(ARG_EMAIL);
            mPhone = getArguments().getString(ARG_PHONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TVName = getView().findViewById(R.id.name);
        TVEmail = getView().findViewById(R.id.email);
        TVPhone = getView().findViewById(R.id.phone);
        IVProfilePic = getView().findViewById(R.id.profilePic);
        IVProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity(ProfileFragment.this);
            }
        });
        TVName.setText(mName);
        TVEmail.setText(mEmail);
        TVPhone.setText(mPhone);

    }

    public void startImageChooserActivity(Fragment fragment) {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            fragment.startActivityForResult(Intent.createChooser(intent, "Choose photo"), 2);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageChooserActivity(ProfileFragment.this);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Bitmap croppedImageFile = Helper.getINSTANCE().getImageFromResult(getContext(),data);
            IVProfilePic.setImageBitmap(croppedImageFile);
        }
    }
}
