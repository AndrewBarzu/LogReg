package com.example.xghos.Wrenchy.main_activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.SwipeRecyclerViewAdapter;
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.helpers_extras.MyOffer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class TakenOffersFragment extends Fragment {

    private ArrayList<MyOffer> mOffers;
    private SwipeRecyclerViewAdapter offerAdapter;
    private ArrayList<String> offerIDs;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TakenOffersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOffers = new ArrayList<>();
        offerIDs = new ArrayList<>();
        offerAdapter = new SwipeRecyclerViewAdapter(getContext(), mOffers, false);
        new GetTakenOffersAsync(this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taken_offers, container, false);
        swipeRefreshLayout = v.findViewById(R.id.refresh_taken_offers);
        RecyclerView mTakenOffers = v.findViewById(R.id.takenOffers);
        mTakenOffers.addItemDecoration(new DividerItemDecoration(mTakenOffers.getContext(), DividerItemDecoration.VERTICAL));
        mTakenOffers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        mTakenOffers.setAdapter(offerAdapter);
        final TakenOffersFragment takenOffersFragment = this;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetTakenOffersAsync(takenOffersFragment).execute();
            }
        });
        return v;
    }

    static class GetTakenOffersAsync extends AsyncTask<String, Void, String> {

        private TakenOffersFragment takenOffersFragment;

        GetTakenOffersAsync(TakenOffersFragment context){
            takenOffersFragment = new WeakReference<>(context).get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            takenOffersFragment.mOffers.clear();
            takenOffersFragment.offerAdapter.notifyDataSetChanged();
            takenOffersFragment.offerIDs.clear();
        }

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_user", CurrentUser.getId());
            getParams.put("request", "getTakenOffers");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/gethybridoffers.php").connect();
                JSONObject responseObject = new JSONObject(response);
                JSONObject object = responseObject.getJSONObject("result");
                JSONArray takenOffers = object.getJSONArray("oferteluate");
                for (int i = 0; i < takenOffers.length(); i++) {
                    MyOffer offer = new MyOffer();
                    offer.setName(takenOffers.getJSONObject(i).getString("titlu_oferta"));
                    offer.setPrice(takenOffers.getJSONObject(i).getString("pret_oferta") + "€");
                    offer.setLocation(takenOffers.getJSONObject(i).getString("nume_locatie"));
                    offer.setOffer_id(takenOffers.getJSONObject(i).getString("id_oferta"));
                    offer.setOfferer(takenOffers.getJSONObject(i).getString("nume_angajator"));
                    if (!takenOffersFragment.offerIDs.contains(takenOffers.getJSONObject(i).getString("id_oferta"))) {
                        takenOffersFragment.offerIDs.add(takenOffers.getJSONObject(i).getString("id_oferta"));
                        takenOffersFragment.mOffers.add(offer);
                    }
                }
            } catch (Exception e) {
                return "nuok";
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            takenOffersFragment.offerAdapter.notifyDataSetChanged();
            takenOffersFragment.swipeRefreshLayout.setRefreshing(false);
        }
    }
}


