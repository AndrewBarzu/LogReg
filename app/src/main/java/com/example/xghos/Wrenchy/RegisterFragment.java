package com.example.xghos.Wrenchy;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
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

import org.json.JSONObject;

import java.util.HashMap;


public class RegisterFragment extends Fragment {

    ConstraintLayout layout;

    EditText ETName;
    EditText ETMail;
    EditText ETPassword;
    EditText ETPhone;
    ImageView IVProfilePic;
    Button BRegister;
    Bitmap croppedImageFile;
    int PIC_CROP = 999;

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
                startImageChooserActivity(getActivity());
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

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_register);
//
//
//        ETName = findViewById(R.id.Name);
//        ETMail = findViewById(R.id.email);
//        ETPassword = findViewById(R.id.ETPassword);
//        ETPhone = findViewById(R.id.ETPhone);
//        IVProfilePic = findViewById(R.id.profilePic);
//
//        final Button BRegister = findViewById(R.id.BRegister);
//
//        layout = findViewById(R.id.RegisterPanel);
//
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
//                }
//            }
//        });
//
//        BRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Helper.getINSTANCE().registerValidation(ETMail.getText().toString(), ETPassword.getText().toString(), ETName.getText().toString(),
//                        ETPhone.getText().toString()))
//                    new RegisterAsyncTask().execute();
//                else
//                    Toast.makeText(RegisterFragment.this, "RegisterFragment Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        IVProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startImageChooserActivity(RegisterFragment.this);
//            }
//        });

//        BRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = ETName.getText().toString();
//                String mail = ETMail.getText().toString();
//                String pass = ETPassword.getText().toString();
//                String conpass = ETConPassword.getText().toString();
//                String accType = "0";
//                if (SType.isChecked())
//                {
//                    accType = "1";
//                }
//
//                if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || conpass.isEmpty()) {
//                    Toast.makeText(RegisterFragment.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//                } else if (!(mail.contains("@") && mail.contains("."))) {
//                    Toast.makeText(RegisterFragment.this, "Your Email address is not valid!", Toast.LENGTH_SHORT).show();
//                } else if (pass.length() < 6) {
//                    Toast.makeText(RegisterFragment.this, "Your Password is too short!", Toast.LENGTH_SHORT).show();
//                } else if (!pass.equals(conpass)) {
//                    Toast.makeText(RegisterFragment.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    db.addUser(new User(null, name, mail, pass, accType));
//                    finish();
//                    Toast.makeText(RegisterFragment.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    public void startImageChooserActivity(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            activity.startActivityForResult(Intent.createChooser(intent, "Choose photo"), 2);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageChooserActivity(getActivity());
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
            cropIntent.putExtra("outputX", 150);
            cropIntent.putExtra("outputY", 150);
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
                final String message = responseObject.getString("response");
                Log.d("+++", message);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (message.equals("Succes."))
                        {
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }

                        else
                            Toast.makeText(getContext(), "nu merge", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }
    }
}

