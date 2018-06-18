package com.example.xghos.Wrenchy;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static android.support.v4.graphics.TypefaceCompatUtil.getTempFile;


public class ProfileFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PHONE = "phone";
    final int PIC_CROP = 999;

    private String mName;
    private String mEmail;
    private String mPhone;
    private TextView TVName;
    private TextView TVEmail;
    private TextView TVPhone;
    private ImageView IVProfilePic;

    private Bitmap croppedImageFile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = currentUser.getUserName();
        mEmail = currentUser.getEmail();
        mPhone = currentUser.getPhoneNumber();
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
        TVName.setText(getString(R.string.hello, mName));
        TVEmail.setText(mEmail);
        TVPhone.setText(mPhone);
        Log.d("+++", currentUser.getAvatar());
        Log.d("+++", currentUser.getEmail());
        if(currentUser.getAvatar().length() > 10){
            IVProfilePic.setImageBitmap(Helper.getINSTANCE().getBitmapFromString(currentUser.getAvatar()));
        }
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

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 100);
            cropIntent.putExtra("outputY", 100);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch (ActivityNotFoundException anfe) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            performCrop(selectedImage);
        }
        if(requestCode == PIC_CROP && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            croppedImageFile = extras.getParcelable("data");
            IVProfilePic.setImageBitmap(croppedImageFile);
            new ChangeProfilePic().execute();
        }
    }

    private class ChangeProfilePic extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            String encoded = Helper.getINSTANCE().getStringFromBitmap(croppedImageFile);
            //Log.d("++++", encoded);
            getParams.put("avatar", encoded);
            getParams.put("id", String.valueOf(currentUser.getId()));
            getParams.put("request", "avatarchange");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/avatarchange.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                String Object = responseObject.getString("response");

                if (message.equals("error"))
                {
                    Log.d("+++", Object);
                }

            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }
    }
}

