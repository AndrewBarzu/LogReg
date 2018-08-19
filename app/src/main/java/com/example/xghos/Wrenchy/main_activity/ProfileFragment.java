package com.example.xghos.Wrenchy.main_activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.Helper;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.helpers_extras.LockableViewPager;
import com.example.xghos.Wrenchy.helpers_extras.NoReviewsFragment;
import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.ViewPagerAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private String mEmail;
    private String mPhone;
    private ImageView IVProfilePic;
    private Bitmap croppedImageFile;
    private Uri mCropImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmail = CurrentUser.getEmail();
        mPhone = CurrentUser.getPhoneNumber();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        LockableViewPager reviewsContainer = v.findViewById(R.id.reviewContainer);
        setupViewPager(reviewsContainer);
        reviewsContainer.setSwipeable(false);

        TabLayout tabLayout = v.findViewById(R.id.reviewTabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(reviewsContainer));

        TextView TVEmail = v.findViewById(R.id.email);
        TextView TVPhone = v.findViewById(R.id.phone);

        IVProfilePic = v.findViewById(R.id.profilePic);
        IVProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
            }
        });

        TVEmail.setText(mEmail);
        TVPhone.setText(mPhone);

        if(CurrentUser.getAvatar().length() > 10){
            IVProfilePic.setImageBitmap(Helper.getINSTANCE().getBitmapFromString(CurrentUser.getAvatar()));
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new NoReviewsFragment());
        adapter.addFragment(new NoReviewsFragment());
        viewPager.setAdapter(adapter);
    }

    public void startImageChooserActivity() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            CropImage.startPickImageActivity(getContext(), this);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageChooserActivity();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                performCrop(mCropImageUri);
            } else {
                Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void performCrop(Uri picUri) {
        CropImage.activity(picUri)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(getContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {

                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {

                performCrop(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                File cropableImage = new File(result.getUri().getPath());
                try {
                    croppedImageFile = new Compressor(getContext())
                            .setMaxWidth(124)
                            .setMaxHeight(124)
                            .setQuality(30)
                            .compressToBitmap(cropableImage);
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Could not compress image file!", Toast.LENGTH_SHORT).show();
                    Log.d("compression exception", e.toString());
                }
                IVProfilePic.setImageBitmap(croppedImageFile);
                new ChangeProfilePic().execute(croppedImageFile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d("+++", result.getError().toString());
            }
        }
    }

    private static class ChangeProfilePic extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... objects) {
            HashMap<String, String> getParams = new HashMap<>();


            String encoded = Helper.getINSTANCE().getStringFromBitmap(objects[0]);
            CurrentUser.setAvatar(encoded);
            getParams.put("avatar", encoded);
            getParams.put("id", String.valueOf(CurrentUser.getId()));
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

