package com.example.xghos.loginregister;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferAdapter extends ArrayAdapter<Integer> {

    /*
    *Adaptorul pentru oferte, aici sunt luate ofertele de pe server si puse in listView prin apasarea unei date afisate in calendar
    Momentan apare doar titlul ofertei
     */

    private Context mContext;
    private ArrayList<String> mOffers;
    private int mResource;

    /*TODO obiect de tip MyOffer in care sa se salveze titlul impreuna cu detaliile despre oferta, pretul acesteia, specializarea, id-ul userului care a postat oferta si locatia
     */


    public OfferAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
        mOffers = new ArrayList<>();
        new GetOffersAsync().execute();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(mResource, null);
        }
        TextView offerNumber = convertView.findViewById(R.id.offerNumber);
        offerNumber.setText(mOffers.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return mOffers.size();
    }

    private class GetOffersAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_user", currentUser.getId());
            getParams.put("request", "getUsers");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/listoffers.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                JSONArray Object = responseObject.getJSONArray("response");
                for(int i = 0; i<Object.length(); i++){
                    mOffers.add(Object.getJSONObject(i).getString("titlu_oferta"));
                }
                Log.d("+++", message);

            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }
    }
}
