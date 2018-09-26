package com.appist.xghos.Wrenchy.main_activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.adapters.SwipeRecyclerViewAdapter;
import com.appist.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.appist.xghos.Wrenchy.helpers_extras.MyOffer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;


public class PostedOffersFragment extends Fragment {

    private ArrayList<MyOffer> mOffers;
    private SwipeRecyclerViewAdapter offerAdapter;
    private ArrayList<String> offerIDs;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View rootView;

    public PostedOffersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mOffers = new ArrayList<>();
            offerIDs = new ArrayList<>();
            offerAdapter = new SwipeRecyclerViewAdapter(getActivity().getIntent().getExtras().getString("id"), getContext(), mOffers, true);
            new GetPostedOffersAsync(this, getActivity().getIntent().getExtras().getString("id")).execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_posted_offers, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        if (savedInstanceState == null) {
            swipeRefreshLayout = rootView.findViewById(R.id.refresh_posted_offers);
            RecyclerView postedOffers = rootView.findViewById(R.id.postedOffers);
            postedOffers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
            postedOffers.setAdapter(offerAdapter);
            final PostedOffersFragment postedOffersFragment = this;
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new GetPostedOffersAsync(postedOffersFragment, getActivity().getIntent().getExtras().getString("id")).execute();
                }
            });
        }
    }

    static class GetPostedOffersAsync extends AsyncTask<String, Void, String> {

        private PostedOffersFragment postedOffersFragment;
        private String mId;

        GetPostedOffersAsync(PostedOffersFragment context, String id) {
            postedOffersFragment = new WeakReference<>(context).get();
            mId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postedOffersFragment.mOffers.clear();
            postedOffersFragment.offerAdapter.notifyDataSetChanged();
            postedOffersFragment.offerIDs.clear();
        }

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_user", mId);
            getParams.put("request", "getTakenOffers");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/gethybridoffers.php").connect();
                JSONObject responseObject = new JSONObject(response);
                JSONObject object = responseObject.getJSONObject("result");
                JSONArray takenOffers = object.getJSONArray("ofertepuse");
                for (int i = 0; i < takenOffers.length(); i++) {
                    MyOffer offer = new MyOffer();
                    offer.setName(takenOffers.getJSONObject(i).getString("titlu_oferta"));
                    offer.setPrice(takenOffers.getJSONObject(i).getString("pret_oferta") + "€");
                    offer.setLocation(takenOffers.getJSONObject(i).getString("nume_locatie"));
                    offer.setOffer_id(takenOffers.getJSONObject(i).getString("id_oferta"));
                    offer.setOfferer(takenOffers.getJSONObject(i).getString("nume_angajator"));
                    if (!postedOffersFragment.offerIDs.contains(takenOffers.getJSONObject(i).getString("id_oferta"))) {
                        postedOffersFragment.offerIDs.add(takenOffers.getJSONObject(i).getString("id_oferta"));
                        postedOffersFragment.mOffers.add(offer);
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
            postedOffersFragment.offerAdapter.notifyDataSetChanged();
            postedOffersFragment.swipeRefreshLayout.setRefreshing(false);
        }
    }
}
