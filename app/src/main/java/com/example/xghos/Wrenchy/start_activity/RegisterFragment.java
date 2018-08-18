package com.example.xghos.Wrenchy.start_activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xghos.Wrenchy.helpers_extras.Helper;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment {

    ConstraintLayout layout;

    EditText ETName;
    EditText ETMail;
    EditText ETPassword;
    EditText ETPhone;
    ImageView IVProfilePic;
    Button BRegister;
    Bitmap croppedImageFile;
    Uri mCropImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ETName = view.findViewById(R.id.ETName);
        ETMail = view.findViewById(R.id.ETEmail);
        ETPassword = view.findViewById(R.id.ETPassword);
        ETPhone = view.findViewById(R.id.ETPhone);
        IVProfilePic = view.findViewById(R.id.IVProfilePic);

        IVProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
            }
        });
        BRegister = view.findViewById(R.id.BRegister);
        BRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.getINSTANCE().registerValidation(ETMail.getText().toString(), ETPassword.getText().toString(), ETName.getText().toString(),
                        ETPhone.getText().toString()))
                    new RegisterAsyncTask().execute();
                else
                    Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

        layout = view.findViewById(R.id.RegisterPanel);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                }
            }
        });
        return view;
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
                Uri resultUri = result.getUri();
                croppedImageFile = Helper.getINSTANCE().getImageResized(getContext(), resultUri);
                IVProfilePic.setImageBitmap(croppedImageFile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d("+++", result.getError().toString());
            }
        }
    }

    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            String name = ETName.getText().toString();
            String mail = ETMail.getText().toString();
            String password = ETPassword.getText().toString();
            String phone = ETPhone.getText().toString();
            String image = "0";
            if(croppedImageFile != null)
                image = Helper.getINSTANCE().getStringFromBitmap(croppedImageFile);
            getParams.put("nume", name);
            getParams.put("prenume", " ");
            getParams.put("mail", mail);
            getParams.put("parola", password);
            getParams.put("telefon", phone);
            getParams.put("tip", String.valueOf(1));
            getParams.put("avatar", image);
            Log.d("+++", image);
            getParams.put("request", "register");


            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/register.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                return message;
            }
            catch (Exception e)
            {
                return "Unknown Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "success":
                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    break;
                case "error":
                    Toast.makeText(getContext(), "nu merge", Toast.LENGTH_SHORT).show();
                    break;
                case "Unknown Error":
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

