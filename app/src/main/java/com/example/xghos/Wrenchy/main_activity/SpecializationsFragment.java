package com.example.xghos.Wrenchy.main_activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.SpecializationsAdapter;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;

public class SpecializationsFragment extends Fragment {

    private ToolbarInterface toolbarInterface;
    private ArrayList<Drawable> selectors;
    private JSONArray pressedButtons;


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
        selectors = new ArrayList<>();
        selectors.add(getResources().getDrawable(R.drawable.j_babysitting_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_cleaning_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_cooking_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_electrician_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_gardener_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_housepainter_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_mechanic_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_plumber_selector));
        selectors.add(getResources().getDrawable(R.drawable.j_shopping_selector));
        pressedButtons = new JSONArray();

        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        GridView specializations = v.findViewById(R.id.specialization_list);

        specializations.setAdapter(new SpecializationsAdapter(getContext(), selectors));

        Button button = v.findViewById(R.id.set_specializations);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbarInterface.showToolbar();
    }
}
