package com.example.xghos.Wrenchy.main_activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.Helper;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;
import com.example.xghos.Wrenchy.interfaces.WindowInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class CreateOfferFragment extends Fragment {

    private ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5;
    private String[] image;
    private Button mPostJob;
    private EditText offerTitle;
    private EditText offerDetails;
    private EditText offerLocation;
    private EditText price;
    private Uri mCropImageUri;
    private Bitmap croppedImageFile;
    private ImageButton lastClicked, nextImage;
    private ToolbarInterface toolbarInterface;

    public CreateOfferFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarInterface = (ToolbarInterface) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_offer, container, false);

        toolbarInterface.setToolbarTitle("Offer details");
        toolbarInterface.cancel();
        imageButton1 = v.findViewById(R.id.imageButton);
        imageButton2 = v.findViewById(R.id.imageButton2);
        imageButton3 = v.findViewById(R.id.imageButton3);
        imageButton4 = v.findViewById(R.id.imageButton4);
        imageButton5 = v.findViewById(R.id.imageButton5);
        mPostJob = v.findViewById(R.id.postJob);
        offerTitle = v.findViewById(R.id.postOfferTitle);
        offerDetails = v.findViewById(R.id.postOfferDetails);
        offerLocation = v.findViewById(R.id.postOfferLocation);
        price = v.findViewById(R.id.postPrice);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
                lastClicked = imageButton1;
                nextImage = imageButton2;
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
                lastClicked = imageButton2;
                nextImage = imageButton3;
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
                lastClicked = imageButton3;
                nextImage = imageButton4;
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
                lastClicked = imageButton4;
                nextImage = imageButton5;
            }
        });

        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooserActivity();
                lastClicked = imageButton5;
                nextImage = null;
            }
        });
        final CreateOfferFragment createOfferFragment = this;
        mPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!offerTitle.getText().toString().equals("") && !offerDetails.getText().toString().equals("")
                        && !offerLocation.getText().toString().equals("") && !price.getText().toString().equals(""))
                    new AddOffer(createOfferFragment).execute();
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        toolbarInterface.add();
        super.onDestroyView();
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
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
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
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Could not compress image file!", Toast.LENGTH_SHORT).show();
                    Log.d("compression exception", e.toString());
                }
                lastClicked.setImageBitmap(croppedImageFile);
                if (nextImage != null) {
                    nextImage.setVisibility(View.VISIBLE);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d("+++", result.getError().toString());
            }
        }
    }

    private static class AddOffer extends AsyncTask<Bitmap, Void, String> {

        CreateOfferFragment createOfferFragment;

        public AddOffer(CreateOfferFragment context) {
            createOfferFragment = new WeakReference<>(context).get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_user", CurrentUser.getId());
            getParams.put("titlu_oferta", createOfferFragment.offerTitle.getText().toString());
            getParams.put("descriere_oferta", createOfferFragment.offerDetails.getText().toString());
            getParams.put("pret_oferta", createOfferFragment.price.getText().toString());
            getParams.put("cod_specializare", "1");
            getParams.put("nume_locatie", "1");
            getParams.put("data_expirare_oferta", "2019-01-01");
            Integer imageCount = 0;
            if (createOfferFragment.imageButton1.getDrawable() != null) {
                getParams.put("imagine_oferta_1", Helper.getINSTANCE().getStringFromBitmap(((BitmapDrawable) createOfferFragment.imageButton1.getDrawable()).getBitmap()));
                imageCount++;
                if (createOfferFragment.imageButton2.getDrawable() != null) {
                    getParams.put("imagine_oferta_2", Helper.getINSTANCE().getStringFromBitmap(((BitmapDrawable) createOfferFragment.imageButton2.getDrawable()).getBitmap()));
                    imageCount++;
                    if (createOfferFragment.imageButton3.getDrawable() != null) {
                        getParams.put("imagine_oferta_3", Helper.getINSTANCE().getStringFromBitmap(((BitmapDrawable) createOfferFragment.imageButton3.getDrawable()).getBitmap()));
                        imageCount++;
                        if (createOfferFragment.imageButton4.getDrawable() != null) {
                            getParams.put("imagine_oferta_4", Helper.getINSTANCE().getStringFromBitmap(((BitmapDrawable) createOfferFragment.imageButton4.getDrawable()).getBitmap()));
                            imageCount++;
                            if (createOfferFragment.imageButton5.getDrawable() != null) {
                                getParams.put("imagine_oferta_5", Helper.getINSTANCE().getStringFromBitmap(((BitmapDrawable) createOfferFragment.imageButton5.getDrawable()).getBitmap()));
                                imageCount++;
                            }
                        }
                    }
                }
            }
            getParams.put("count_images", imageCount.toString());

            getParams.put("request", "add_offer");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/addoffer.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                return message;

            } catch (Exception e) {
                return "nuok";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "success":
                    Toast.makeText(createOfferFragment.getActivity(), "Offer created!", Toast.LENGTH_SHORT).show();
                    createOfferFragment.getActivity().getSupportFragmentManager().popBackStack();
                    break;
                case "error":
                    Toast.makeText(createOfferFragment.getContext(), "Your offer could not be posted!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
