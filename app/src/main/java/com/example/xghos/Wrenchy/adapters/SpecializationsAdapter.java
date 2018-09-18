package com.example.xghos.Wrenchy.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;

import com.example.xghos.Wrenchy.R;

import java.util.ArrayList;

public class SpecializationsAdapter extends ArrayAdapter {

    private ArrayList<Drawable> objects;
    private Context context;

    public SpecializationsAdapter(Context context, ArrayList<Drawable> objects){
        super(context, R.layout.specialization_item, objects);
        this.context = context;
        this.objects = objects;
    }

    class MyHolder{
        ToggleButton button;
    }

    int lastPosition = -1;

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MyHolder myHolder;
        final View result;

        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.specialization_item, parent, false);
            myHolder.button = convertView.findViewById(R.id.specialization_toggle);
            myHolder.button.setBackgroundDrawable(objects.get(position));
            Point size = new Point();
            ((Activity)context).getWindowManager().getDefaultDisplay().getSize(size);
            convertView.getLayoutParams().height = size.x/2;
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        return convertView;
    }

}
