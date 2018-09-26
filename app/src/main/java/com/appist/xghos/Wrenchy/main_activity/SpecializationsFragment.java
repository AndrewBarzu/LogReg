package com.appist.xghos.Wrenchy.main_activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.appist.xghos.Wrenchy.interfaces.ToolbarInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpecializationsFragment extends Fragment {

    ToolbarInterface toolbarInterface;
    private List<ToggleButton> buttons = new ArrayList<>();
    private ConstraintLayout loadingScreen;


    public SpecializationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarInterface = (ToolbarInterface) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_specializations, container, false);

        toolbarInterface.hideToolbar();
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        buttons.add((ToggleButton) v.findViewById(R.id.babysitting));
        buttons.add((ToggleButton) v.findViewById(R.id.cleaning));
        buttons.add((ToggleButton) v.findViewById(R.id.cooking));
        buttons.add((ToggleButton) v.findViewById(R.id.electrician));
        buttons.add((ToggleButton) v.findViewById(R.id.gardener));
        buttons.add((ToggleButton) v.findViewById(R.id.housepainter));
        buttons.add((ToggleButton) v.findViewById(R.id.mechanic));
        buttons.add((ToggleButton) v.findViewById(R.id.plumber));
        buttons.add((ToggleButton) v.findViewById(R.id.shopping));
        final String id;
        if (getActivity().getIntent().getExtras() != null)
            id = getActivity().getIntent().getExtras().getString("id");
        else id = "0";

        final SpecializationsFragment context = this;

        loadingScreen = v.findViewById(R.id.specializations_LS);

        Button button = v.findViewById(R.id.set_specializations);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetSpecializations(context).execute(id);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbarInterface.showToolbar();
    }

    private static class SetSpecializations extends AsyncTask<String, Void, String> {

        private SpecializationsFragment specializationsFragment;

        SetSpecializations(SpecializationsFragment specializationsFragment) {
            this.specializationsFragment = specializationsFragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            specializationsFragment.loadingScreen.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            final JSONArray pressedButtons = new JSONArray();
            final JSONArray notPressedButtons = new JSONArray();

            for (int i = 0; i < 9; i++) {
                if (specializationsFragment.buttons.get(i).isChecked()) {
                    pressedButtons.put(String.valueOf(i + 1));
                } else
                    notPressedButtons.put(String.valueOf(i + 1));
            }

            getParams.put("id_user", objects[0]);
            getParams.put("adaugare", pressedButtons.toString());
            getParams.put("request", "specializari");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/specializari.php").connect();
                JSONObject responseObject = new JSONObject(response);

            } catch (Exception e) {
                return "ERROR";
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "ok":
                    specializationsFragment.getActivity().getSupportFragmentManager().popBackStack();
                    Toast.makeText(specializationsFragment.getActivity(), "Specializations changed successfully!", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR":
                    Toast.makeText(specializationsFragment.getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            specializationsFragment.loadingScreen.setVisibility(View.GONE);
        }
    }
}
