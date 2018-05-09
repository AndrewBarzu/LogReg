package com.example.xghos.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;


public class Helper {

    private static Helper INSTANCE;

    private Helper() {
    }

    public static Helper getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Helper();
        }
        return INSTANCE;
    }

    public boolean loginValidation(String email, String password){
        if(email.isEmpty() || password.isEmpty())
            return false;
        if(password.length()<6)
            return false;
        if(!isEmail(email))
            return false;
        return true;
    }

    public boolean registerValidation(String email, String password, String name, String surname, String phone, String conPassword){
        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || phone.isEmpty() || conPassword.isEmpty())
            return false;
        if(password.length()<6)
            return false;
        if (!password.equals(conPassword))
            return false;
        if(!Patterns.PHONE.matcher(phone).matches())
            return false;
        if(!isEmail(email))
            return false;
        return true;
    }

    public boolean isEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public Bitmap getImageFromResult(Context context, Intent imageReturnedIntent) {
        Bitmap bm = null;
        File imageFile = new File(context.getFilesDir(), "profImage.jpg");
        Uri selectedImage;
        boolean isCamera = (imageReturnedIntent == null || imageReturnedIntent.getData() == null || imageReturnedIntent.getData().toString().contains(imageFile.toString()));
        if (isCamera) {
            selectedImage = Uri.fromFile(imageFile);
        } else {
            selectedImage = imageReturnedIntent.getData();
        }

        bm = getImageResized(context, selectedImage);
        return bm;
    }

    public Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            i++;
        } while (bm.getWidth() < 400 && i < sampleSizes.length);
        return bm;
    }

    public Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        return actuallyUsableBitmap;
    }
}
