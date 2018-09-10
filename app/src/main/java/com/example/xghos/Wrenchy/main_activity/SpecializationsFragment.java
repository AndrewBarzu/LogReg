package com.example.xghos.Wrenchy.main_activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SpecializationsFragment extends Fragment {

    ToolbarInterface toolbarInterface;


    public SpecializationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarInterface = (ToolbarInterface)getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_specializations, container, false);

        toolbarInterface.hideToolbar();

        final List<ToggleButton> buttons = new ArrayList<>();

        buttons.add((ToggleButton) v.findViewById(R.id.babysitting));
        buttons.add((ToggleButton) v.findViewById(R.id.cleaning));
        buttons.add((ToggleButton) v.findViewById(R.id.cooking));
        buttons.add((ToggleButton) v.findViewById(R.id.electrician));
        buttons.add((ToggleButton) v.findViewById(R.id.gardener));
        buttons.add((ToggleButton) v.findViewById(R.id.housepainter));
        buttons.add((ToggleButton) v.findViewById(R.id.mechanic));
        buttons.add((ToggleButton) v.findViewById(R.id.plumber));
        buttons.add((ToggleButton) v.findViewById(R.id.shopping));

        final JSONArray pressedButtons = new JSONArray();

        Button button = v.findViewById(R.id.set_specializations);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                for (int i = 0; i<9; i++){
                    if(buttons.get(i).isChecked()){
                        pressedButtons.put(String.valueOf(i+1));
                    }
                }
                Log.d("buttons", pressedButtons.toString());
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbarInterface.showToolbar();
    }
}
